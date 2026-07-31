// ShopNest Main JS

document.addEventListener('DOMContentLoaded', () => {

  // ===== Auto-hide alerts =====
  document.querySelectorAll('.alert').forEach(el => {
    setTimeout(() => {
      el.style.transition = 'opacity 0.5s';
      el.style.opacity = '0';
      setTimeout(() => el.remove(), 500);
    }, 4000);
  });

  // ===== Search form =====
  const searchInput = document.querySelector('.navbar-search input');
  if (searchInput) {
    searchInput.addEventListener('keypress', e => {
      if (e.key === 'Enter') {
        const q = searchInput.value.trim();
        if (q) window.location.href = `/shop?search=${encodeURIComponent(q)}`;
      }
    });
  }

  // ===== Cart quantity controls =====
  document.querySelectorAll('.qty-btn').forEach(btn => {
    btn.addEventListener('click', function () {
      const action = this.dataset.action;
      const form = this.closest('.qty-form');
      const qtyInput = form.querySelector('.qty-val');
      let val = parseInt(qtyInput.value);
      if (action === 'plus') val++;
      if (action === 'minus' && val > 1) val--;
      qtyInput.value = val;
      form.submit();
    });
  });

  // ===== Payment method selection =====
  document.querySelectorAll('.payment-option').forEach(opt => {
    opt.addEventListener('click', function () {
      document.querySelectorAll('.payment-option').forEach(o => o.classList.remove('selected'));
      this.classList.add('selected');
      this.querySelector('input[type="radio"]').checked = true;
      // Show/hide card fields
      const cardFields = document.getElementById('card-fields');
      if (cardFields) {
        const method = this.querySelector('input').value;
        cardFields.style.display = (method === 'CREDIT_CARD' || method === 'DEBIT_CARD') ? 'block' : 'none';
      }
    });
  });

  // ===== Card number formatting =====
  const cardNum = document.getElementById('cardNumber');
  if (cardNum) {
    cardNum.addEventListener('input', function () {
      let v = this.value.replace(/\D/g, '').substring(0, 16);
      this.value = v.replace(/(.{4})/g, '$1 ').trim();
    });
  }

  const expiry = document.getElementById('expiry');
  if (expiry) {
    expiry.addEventListener('input', function () {
      let v = this.value.replace(/\D/g, '');
      if (v.length >= 2) v = v.substring(0, 2) + '/' + v.substring(2, 4);
      this.value = v;
    });
  }

  // ===== Smooth scroll =====
  document.querySelectorAll('a[href^="#"]').forEach(a => {
    a.addEventListener('click', function (e) {
      const target = document.querySelector(this.getAttribute('href'));
      if (target) {
        e.preventDefault();
        target.scrollIntoView({ behavior: 'smooth' });
      }
    });
  });

  // ===== Animate on scroll =====
  const observer = new IntersectionObserver(entries => {
    entries.forEach(e => {
      if (e.isIntersecting) {
        e.target.classList.add('visible');
        observer.unobserve(e.target);
      }
    });
  }, { threshold: 0.1 });

  document.querySelectorAll('.product-card, .category-card').forEach(el => {
    el.style.opacity = '0';
    el.style.transform = 'translateY(20px)';
    el.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
    observer.observe(el);
  });

  document.querySelectorAll('.visible').forEach(el => {
    // handled by observer
  });

  // Add visible class style
  const style = document.createElement('style');
  style.textContent = `.visible { opacity: 1 !important; transform: translateY(0) !important; }`;
  document.head.appendChild(style);
});
