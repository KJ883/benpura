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
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const today = new Date();
            let popupContent = '<h2>週間注文内容</h2>';
            popupContent += '<table>';
            popupContent += `
                <thead>
                    <tr>
            `;

            const daysOfWeek = ['日曜日', '月曜日', '火曜日', '水曜日', '木曜日', '金曜日', '土曜日'];
            const todayIndex = today.getDay();
            for (let i = 0; i < 7; i++) {
                const dayOfWeek = daysOfWeek[(todayIndex + i) % 7];
                popupContent += `<th>${dayOfWeek}</th>`;
            }
            popupContent += '</tr><tr>';

            const dates = [];
            for (let i = 0; i < 7; i++) {
                const currentDate = new Date(today);
                currentDate.setDate(today.getDate() + i);
                dates.push(currentDate);
                const formattedDate = currentDate.toISOString().split('T')[0];
                popupContent += `<th>${formattedDate}</th>`;
            }
            popupContent += '</tr></thead><tbody>';

            for (let i = 0; i < 7; i++) {
                const date = dates[i];
                const formattedDate = date.toISOString().split('T')[0];
                const dayOrders = data[formattedDate] || [];
                let cellClass = '';

                if (date.toDateString() === today.toDateString()) {
                    cellClass = 'today';
                } else if (dayOrders.length > 0) {
                    cellClass = 'hasOrders';
                }

                popupContent += '<tr>';

                // 日付のセルを追加
                popupContent += `<td class="${cellClass}">${formattedDate}</td>`;

                // 注文のセルを追加
                if (dayOrders.length > 0) {
                    dayOrders.forEach(order => {
                        popupContent += `<td class="${cellClass}">
                            <div>${order.item}</div>
                            <div>${order.shopname}</div>
                            <div>${order.date}</div>
                        </td>`;
                    });
                } else {
                    popupContent += `<td class="${cellClass}">注文なし</td>`;
                }

                popupContent += '</tr>';
            }

            popupContent += '</tbody></table><button onclick="closePopup()">閉じる</button>';

            const popup = document.createElement('div');
            popup.classList.add('popup');
            popup.innerHTML = popupContent;
            document.body.appendChild(popup);
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}

function closePopup() {
    const popup = document.querySelector('.popup');
    if (popup) {
        document.body.removeChild(popup);
    }
}


