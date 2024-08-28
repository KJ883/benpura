package com.example.demo.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.ShopListDto;
import com.example.demo.entity.ShopList;
import com.example.demo.form.Form;
import com.example.demo.repository.ShopListRepository;
import com.example.demo.service.ShopListService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopListController {

	// セッション準備 HttpSession型のフィールドを定義する
	private HttpSession session; //若松：注文日時を保存するために必要

	@Autowired
	private ShopListRepository repository;
	@Autowired
	ShopListService shopListService;

	@Autowired //若松：注文日時を保存するために必要
	public void SessionController(HttpSession session) {
		// フィールドに代入する
		this.session = session;
	}

	//	「http://localhost:8080/shopList」
	@GetMapping("/shopList")
	public String shopListShow(Model model) {
		//		Iterable<ShopList> imagedata = repository.findAll(); ↓に変更！
		//		 EntityのデータベースのデータをRepositoryでゲットして、Serviceでどのメソッドを使うか定めて、
		//		Implで内容を書く。それをControllerで反映
		Iterable<ShopList> alldata = shopListService.selectAll();// すべての店舗情報を取得
//		List<String> list = new ArrayList<>();
		List<ShopListDto> shopDtoList = new ArrayList<>();

		// 写真の表示
		for (ShopList shop : alldata) {
			// nullのデータがデータベースにあったらエラーでるのでif分でnull大丈夫にしたげる
			if (shop.getShopPicture() != null) {
				String pictureString = Base64.getEncoder().encodeToString(shop.getShopPicture());
				shopDtoList.add(new ShopListDto(shop.getId(),shop.getShopName(),shop.getShopAddress(),shop.getShopTel(),shop.getShopHour(),shop.getHoliday(),pictureString));
			}
		}
		model.addAttribute("shopLists", shopDtoList); // 左はhtml側で呼び出す為の名前
		return "shopList";
	}

	// 日付確認用にGPTが教えてくれたやつ
	@GetMapping("/check-holiday")
	// selectDateは送られてきた曜日情報が入る場所
	public String shopListCheckHoliday(Model model, Form f) {
		
		//
		// ★serviceでメソッド作成
		// メソッド名：getDayOfWeek
		//
		// 機能①：注文日時の曜日の取得と引き渡し
		// yyyy-MM-dd 形式の日付文字列
		LocalDate date = f.getOrderdate();
		// 取得：注文日時の曜日
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		// 変換：注文日時の曜日をString型へ
		String orderDayOfWeek = dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.JAPANESE);		
		//
		// 機能②：注文の日にちと時間を結合してセッションに注文日時を保存
		//
		// メソッド名 combineDateTime
		//
		// StringをLocalTimeに変換
		LocalTime time = LocalTime.parse(f.getOrdertime(), DateTimeFormatter.ofPattern("HH:mm"));
		// LocalDateとLocalTimeを結合してLocalDateTimeを作成
		LocalDateTime orderdatetime = LocalDateTime.of(f.getOrderdate(), time);
		// ★注文日時をセッションに保存★
		this.session.setAttribute("orderdatetime", orderdatetime);

		//
		// 機能③：お店の情報を取得
		//
		Iterable<ShopList> allShops = shopListService.selectAll();
		// お店のidをキーとして各店舗ごとに定休日を保存
		Map<Integer, String[]> storeHolidays = new HashMap<>();
		List<ShopListDto> tempOpenShop = new ArrayList<>();
		

		//
		// メソッド名：getPictureShopList
		//
		// nullのデータがデータベースにあったらエラーでるのでif分でnull大丈夫にしたげる
		// いったんすべての店舗情報と写真情報を取得
		for (ShopList tempShopList : allShops) {
			if (tempShopList.getShopPicture() != null) {
				String pictureString = Base64
						.getEncoder().encodeToString(tempShopList.getShopPicture());
				tempOpenShop.add(new ShopListDto(tempShopList.getId(),
						tempShopList.getShopName(),
						tempShopList.getShopAddress(),
						tempShopList.getShopTel(),
						tempShopList.getShopHour(),
						tempShopList.getHoliday(),pictureString));
			}

		}
		
		//
		// メソッド名：getHolidayShop
		//
		// 店舗idをキーとして各店舗の定休日を保存
		for (ShopList shop : allShops) {
			Integer shopId = shop.getId();// 店舗idを取得
			String holidays = shop.getHoliday();// 店舗の定休日１列を取得
			String[] holidayArray = holidays.split("・");// 定休日を・で区切って配列に分割
			storeHolidays.put(shopId, holidayArray);
		}
		
		//
		// メソッド名：getIDHolidayShop
		//
		// 店舗idごとに定休日判定する
		// 定休日でない店舗idを保存する
		List<Integer> storeIdList = new ArrayList<>();
		for (Map.Entry<Integer, String[]> entry : storeHolidays.entrySet()) {

			// 店舗idごとの定休日を取得して繰り返す
			String[] holidayList = entry.getValue();
			boolean boolholiday=true;
			for (String tempholiday : holidayList) {
				// 店舗の定休日判定  最初の１文字目だけで取得
				// 注文日の曜日が定休日のひとつでも一致すればOUT判定&breakStop
				if (tempholiday.charAt(0) == orderDayOfWeek.charAt(0)) {
					boolholiday=false;
					break;
				}
			}
			// すべての定休日を判定後、trueのままであれば店舗idを取得
			if(boolholiday) {
				storeIdList.add(entry.getKey());				
			}
		}
		//
		// メソッド名：getOpenShopList
		//
		// 定休日の店舗情報idを使って定休日の店舗は格納しない
		List<ShopListDto> openShop = new ArrayList<>();
		for (Integer tempStoreId : storeIdList) {
			for (ShopListDto temp : tempOpenShop) {
				if (temp.getId() == tempStoreId) {
					openShop.add(temp);
				}
			}
		}
		
		// 定休日を除いた店舗情報を格納
		model.addAttribute("shopLists", openShop); // 左はhtml側で呼び出す為の名前
		
		return "shopList";
		
	}
	
	@GetMapping("/shopInformation")
	public String shopInformationShow(@RequestParam("id") Integer id, Model model) {
	    // IDで店舗情報を取得
	    Optional<ShopList> optionalShop = repository.findById(id);
	    
	    // 店舗情報が存在する場合
	    if (optionalShop.isPresent()) {
	        ShopList shop = optionalShop.get();
	        // 店舗情報から写真を取得し、Base64にエンコード
	        String pictureString = null;
	        if (shop.getShopPicture() != null) {
	            pictureString = Base64.getEncoder().encodeToString(shop.getShopPicture());
	        }
	        
	        // DTOを作成
	        ShopListDto shopDto = new ShopListDto(
	            shop.getId(),
	            shop.getShopName(),
	            shop.getShopAddress(),
	            shop.getShopTel(),
	            shop.getShopHour(),
	            shop.getHoliday(),
	            pictureString
	        );
	        
	        // モデルにDTOを追加
	        model.addAttribute("shop", shopDto);
	    } else {
	        // 店舗が見つからない場合の処理
	        return "redirect:/errorPage"; // エラーページにリダイレクト
	    }

	    return "shopInformation";
	}
	
	
//	@GetMapping("/shopInformation")
//	public String shopInformationShow(Model model) {
//		Iterable<ShopList> alldata2 = repository.findAll(); // すべての店舗情報を取得
//		List<String> list = new ArrayList<>();
//		List<ShopListDto> shopDtoList = new ArrayList<>();
//
//		// 写真の表示
//		for (ShopList shop : alldata2) {
//			// nullのデータがデータベースにあったらエラーでるのでif分でnull大丈夫にしたげる
//			if (shop.getShopPicture() != null) {
//				String pictureString = Base64.getEncoder().encodeToString(shop.getShopPicture());
//				shopDtoList.add(new ShopListDto(shop.getId(),shop.getShopName(),shop.getShopAddress(),shop.getShopTel(),shop.getShopHour(),shop.getHoliday(),pictureString));
//			}
//		}
//		model.addAttribute("shopLists", shopDtoList);
//
//		return "shopInformation";
//	}

}
