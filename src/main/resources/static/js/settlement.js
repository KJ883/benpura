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
	fetch('/api/orders/week')
		.then(response => response.json())
		.then(data => {
			const today = new Date();
			let popupContent = '<h2>週間注文内容</h2>';
			popupContent += `
                        <table>
                            <tr>
                                <th>月曜日</th>
                                <th>火曜日</th>
                                <th>水曜日</th>
                                <th>木曜日</th>
                                <th>金曜日</th>
                                <th>土曜日</th>
                                <th>日曜日</th>
                            </tr>
                            <tr>
                    `;

			// 今日から一週間の日付を取得
			const daysOfWeek = ['月曜日', '火曜日', '水曜日', '木曜日', '金曜日', '土曜日', '日曜日'];
			for (let i = 0; i < daysOfWeek.length; i++) {
				const currentDate = new Date(today);
				currentDate.setDate(today.getDate() + i);
				const formattedDate = currentDate.toISOString().split('T')[0];

				let dayOrders = data[formattedDate] || [];
				let cellClass = '';
				if (currentDate.toDateString() === today.toDateString()) {
					cellClass = 'today';
				} else if (dayOrders.length > 0) {
					cellClass = 'hasOrders';
				}

				popupContent += `<td class="${cellClass}">`;
				popupContent += `<div><strong>${formattedDate}</strong></div>`;
				dayOrders.forEach(order => {
					popupContent += `<div>${order.storeName}<br>${order.details}</div><br>`;
				});
				popupContent += '</td>';
			}

			popupContent += '</tr></table>';

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