document.addEventListener('DOMContentLoaded', (event) => {
  const containers = document.querySelectorAll('.container');
  containers.forEach(container => {
    container.classList.add('is-animated');
  });
});
// クラスの付け外しのみ
const text = document.querySelector('.text');

text.classList.add('is-active');

setInterval(() => {
  text.classList.toggle('is-active');
}, 3000);