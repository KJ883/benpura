document.addEventListener('DOMContentLoaded', (event) => {
  const containers = document.querySelectorAll('.container');
  containers.forEach(container => {
    container.classList.add('is-animated');
  });
});