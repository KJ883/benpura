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
            popupContent += '</tr></thead><tbody><tr>';

            const dates = [];
            for (let i = 0; i < 7; i++) {
                const currentDate = new Date(today);
                currentDate.setDate(today.getDate() + i);
                dates.push(currentDate);
                const formattedDate = currentDate.toISOString().split('T')[0];
                let cellClass = '';

                if (currentDate.toDateString() === today.toDateString()) {
                    cellClass = 'today';
                } else if (data[formattedDate] && data[formattedDate].length > 0) {
                    cellClass = 'hasOrders';
                }

                const formattedDateString = `${currentDate.getMonth() + 1}/${currentDate.getDate()}`;

                popupContent += `<td class="${cellClass}"><div style="border-bottom: 1px solid #000;">${formattedDateString}</div>`;

                const dayOrders = data[formattedDate] || [];
                if (dayOrders.length > 0) {
                    dayOrders.forEach(order => {
                        const orderDate = new Date(order.date);
                        const formattedOrderDate = orderDate.toLocaleString('ja-JP', {
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit'
                        }).replace(/T/, ' ');

                        popupContent += `<div style="border-bottom: 1px solid #ccc; margin-bottom: 5px;">
                            <div>${order.item}</div>
                            <div>${order.shopname}</div>
                            <div>${formattedOrderDate}</div>
                        </div>`;
                    });
                } else {
                    popupContent += '<div>注文なし</div>';
                }
                popupContent += '</td>';
            }
            popupContent += '</tr></tbody></table><button onclick="closePopup()">閉じる</button>';

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
        popup.classList.add('hide');
        popup.addEventListener('animationend', () => {
            document.body.removeChild(popup);
        }, { once: true });
    }
}
