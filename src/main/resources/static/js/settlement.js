// 初期価格を設定します（例: 10000円）
let unitPrice = parseFloat(initialPrice);

// 数量を増やす関数
function increaseQuantity() {
	let quantityInput = document.getElementById('quantity');
	quantityInput.value = parseInt(quantityInput.value) + 1;
	updateTotal();
}

// 数量を減らす関数
function decreaseQuantity() {
	let quantityInput = document.getElementById('quantity');
	if (quantityInput.value > 1) {
		quantityInput.value = parseInt(quantityInput.value) - 1;
		updateTotal();
	}
}

// 合計金額を更新する関数
function updateTotal() {
	let quantity = document.getElementById('quantity').value;
	let totalPrice = Math.floor(unitPrice * quantity)
	document.getElementById('totalPrice').textContent = totalPrice + '円';
	document.getElementById('totalPriceInput').value = totalPrice;
}

// ページ読み込み時に合計金額を初期化
document.addEventListener('DOMContentLoaded', function() {
	updateTotal();
});

document.addEventListener('DOMContentLoaded', () => {
	document.getElementById('openPopup').addEventListener('click', openPopup);
});

function openPopup() {
	// ここでサーバーから注文情報を取得します
	fetch('/api/orders/week')
		.then(response => response.json())
		.then(data => {
			// ポップアップの内容を設定します
			let popupContent = '<h2>週間注文内容</h2>';
			popupContent += '<table><tr><th>日付</th><th>曜日</th><th>店名</th><th>内容</th></tr>';
			data.forEach(order => {
				popupContent += `<tr><td>${order.date}</td><td>${order.dayOfWeek}</td><td>${order.storeName}</td><td>${order.details}</td></tr>`;
			});
			popupContent += '</table>';

			// ポップアップ要素に内容を設定して表示します
			const popup = document.createElement('div');
			popup.classList.add('popup');
			popup.innerHTML = popupContent + '<button onclick="closePopup()">閉じる</button>';
			document.body.appendChild(popup);
		});
}

function closePopup() {
	const popup = document.querySelector('.popup');
	if (popup) {
		document.body.removeChild(popup);
	}
}