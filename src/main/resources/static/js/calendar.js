// 現在表示されている日付を保持する変数
let currentDate = new Date();

// 今日の日付を取得
const today = new Date().toDateString();

//
// 注文履歴を取得する関数
//
function processEvents() {
	// すべてのイベントを取得
	const events = document.querySelectorAll('[data-event-date]');
	const returnEventData = [];
	//
	// すべてのイベント要素を処理
	//
	events.forEach(eventElement => {
		const dateString = eventElement.getAttribute('data-event-date');
		const eventDate = new Date(dateString);
		const eventItem = eventElement.getAttribute('data-event-item');

		// イベントの日付を 'yyyy-mm-dd' の文字形式に変換
		// 関数formatDateStringを使用
		const eventDateString = formatDateToString(eventDate);
		// 日付とイベント名を格納した配列を作成
		const temp = [eventDateString, eventItem];
		// その配列を eventData に追加
		returnEventData.push(temp);
	});
	return returnEventData;
}

//
// カレンダーを更新する関数
//
function updateCalendar() {
	const calendarBody = document.getElementById('calendar-body');
	const monthYear = document.getElementById('month-year');
	const dateSelect = document.getElementById('date-select');
	const timeSelect = document.getElementById('time-select');
	calendarBody.innerHTML = ''; // カレンダーの中身をクリア
	dateSelect.innerHTML = ''; // 日付の選択肢をクリア

	// 現在の月の最初の日と最後の日を取得
	const year = currentDate.getFullYear();
	const month = currentDate.getMonth();
	const firstDay = new Date(year, month, 1);
	const lastDay = new Date(year, month + 1, 0);

	// 月と年を表示
	const options = { year: 'numeric', month: 'long' };
	monthYear.textContent = currentDate.toLocaleDateString('ja-JP', options);

	// 最初の日の曜日を取得
	const startDay = (firstDay.getDay() + 6) % 7; // 月曜始まりに修正

	//
	//１週間先の日付を定義
	//
	const today2 = new Date();
	const endDate = new Date(today2);
	endDate.setDate(today2.getDate() + 7);
	// 来月の最初の日
//	const nextMonth = new Date(today2.getFullYear(), today2.getMonth() + 1, 1);
	// 日付の範囲を決定
//	const maxDate = endDate > nextMonth ? endDate : nextMonth;

	// 今日から最大日付までのオプションを追加
	for (let date = today2; date <= endDate; date.setDate(date.getDate() + 1)) {
		const option = document.createElement('option');
		option.value = formatDateToString(date);
		option.textContent = formatDateToString(date)+ ' (' + getDayName(date) + ')';
		dateSelect.appendChild(option);
	}
	// 曜日の追加関数
	function getDayName(date) {
    const days = ['日', '月', '火', '水', '木', '金', '土'];
    return days[date.getDay()];
}
	// カレンダーの日付を埋める
	let row = document.createElement('tr');
	for (let i = 0; i < startDay; i++) {
		row.appendChild(document.createElement('td')); // 空白セルを追加
	}

	// 月の日付を埋める
	for (let day = 1; day <= lastDay.getDate(); day++) {
		const cell = document.createElement('td');
		const cellDate = new Date(year, month, day);
		// 関数 formatDateToStirng：日時から時間をなくして日付のみにする

		cell.textContent = day;
		//
		// 今日の日付の場合、クラスを追加して強調表示
		//	
		if (cellDate.toDateString() === today) {
			cell.classList.add('today');
		}
		//
		// 注文履歴を読み込む
		//
		const orderList = processEvents();
		//
		// 条件判断：カレンダーの日付と注文履歴の日時が同じであれば
		// 注文内容をセルに追加
		//
		orderList.forEach(event => {
			if (event[0] === formatDateToString(cellDate)) {
				const eventElement = document.createElement('div');
				eventElement.classList.add('event');
				eventElement.textContent = event[1];
				cell.appendChild(eventElement);
			}
		});
		//
		// list型rowの末尾にlist cellを追加
		//
		row.appendChild(cell);

		// 週の終わりに行を追加
		if ((startDay + day) % 7 === 0) {
			calendarBody.appendChild(row);
			row = document.createElement('tr');
		}

	}


	// 最後の行に空白セルを追加して、7列に調整
	if (row.children.length > 0) {
		while (row.children.length < 7) {
			row.appendChild(document.createElement('td'));
		}
		calendarBody.appendChild(row);
	}

	// 時間の選択肢を設定（30分おき）
	for (let hour = 8; hour < 21; hour++) {
		for (let minute = 0; minute < 60; minute += 30) {
			const option = document.createElement('option');
			option.value = `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
			option.textContent = `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
			timeSelect.appendChild(option);
		}
	}
}
// 日付を文字列に変換する関数
function formatDateToString(date) {
	const year = date.getFullYear();
	const month = (date.getMonth() + 1).toString().padStart(2, '0');
	const day = date.getDate().toString().padStart(2, '0');
	return `${year}-${month}-${day}`;
}
//  ★日付から時間を削除できない★
//    const cellDateString = cellDate.toISOString().split('T')[0];
//    ↑toISOstringを使っていた修正元のプログラム文です。
//    なぜか時間がtoISOStringでは削除できなかった
//
//	★プルダウンの日付を文字列に変換した理由★
//	  JavaScriptのtoISOString()メソッドを使って日付をISO 8601形式に変換すると、
//    その日付はUTC（協定世界時）に基づいた時間で表されます。
//	  もしローカル時間がUTCよりも進んでいる場合、
//    日付が前日のものになることがあります。
//  
//	★解決策★
//	  toISOString()ではなく、ローカルタイムを基にした
//	  日付文字列を生成する方法を使うと、ズレを防げます。
//
//  ★問題点★
//    日時のプルダウンが先月の日が選択できるようになっていた
//    上のfunction formatDateToString(date) を追加したら直りました。
//

// 月を変更する関数
function changeMonth(offset) {
	currentDate.setMonth(currentDate.getMonth() + offset);
	initialize()
}

// 関数①カレンダー表示、関数②イベント取得、2つの関数をまとめた
//DOMContenLoadedの重複を避けるため
function initialize() {
	updateCalendar();
}

// DOMが完全に読み込まれたら初期化関数を実行
if (document.readyState === 'loading') {
	document.addEventListener('DOMContentLoaded', initialize);
} else {
	initialize(); // すでに読み込まれている場合は直接呼び出す
}

//document.addEventListener('DOMContentLoaded', () => { ... }); は、
//  JavaScriptでDOM（Document Object Model）が
//  完全に読み込まれたことを確認するために使用するイベントリスナーのコードです。
//  ここでのDOMContentLoaded イベントは、ページ内のすべてのHTMLが完全に読み込ます。
//
//  詳細な説明
//  ①addEventListener メソッドは、
//      指定したイベントが発生したときに実行される関数（イベントリスナー）を追加します。
//  ②'DOMContentLoaded' イベントは、
//    ページのDOMが完全に構築されたときに発生します。
//	  これにより、スクリプトは
//	  HTMLドキュメント内のすべての要素にアクセスできるようになりますが、
//  ③() => { ... }:
//      これはアロー関数（ES6の機能）で、
//	  DOMContentLoaded イベントが発生したときに実行される関数を定義します。
//  使用目的
//    DOMの操作
//	  JavaScriptでDOMの操作（要素の取得や変更）を行う場合、
//	  DOMが完全に読み込まれてから操作する必要があります。
//	  これにより、スクリプトがHTML要素を正しく取得し、操作できるようになります。
//  スクリプトの実行タイミング
//    ページが完全に読み込まれてからスクリプトを実行することで、
//	  ページが正しく表示された状態でスクリプトが実行されることを保証します。
//	  これにより、スクリプトが想定通りに動作しないリスクを避けることができます。
//  ④ ★注意★ スクリプトの重複
//  すでに別の箇所で DOMContentLoaded イベントが設定されている場合、
//  同じイベントが複数回設定されていると、意図した通りに動作しないことがあります。
//  そのため関数の中に関数をいれてひとつにまとめた