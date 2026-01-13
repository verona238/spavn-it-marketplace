export default function Footer() {
  return (
    <footer style={{
      backgroundColor: 'var(--text-primary)',
      color: 'white',
      padding: '2rem 0',
      marginTop: 'auto',
    }}>
      <div className="container">
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
          gap: '2rem',
          marginBottom: '2rem',
        }}>
          <div>
            <h4 style={{ marginBottom: '1rem', color: 'white' }}>О компании</h4>
            <p style={{ fontSize: '0.875rem', opacity: 0.8, lineHeight: 1.6 }}>
              SpavnIT Marketplace - современная платформа для онлайн-покупок
            </p>
          </div>

          <div>
            <h4 style={{ marginBottom: '1rem', color: 'white' }}>Покупателям</h4>
            <ul style={{ listStyle: 'none', fontSize: '0.875rem', lineHeight: 2 }}>
              <li><a href="#" style={{ opacity: 0.8, transition: 'var(--transition)' }}>Как сделать заказ</a></li>
              <li><a href="#" style={{ opacity: 0.8, transition: 'var(--transition)' }}>Оплата</a></li>
              <li><a href="#" style={{ opacity: 0.8, transition: 'var(--transition)' }}>Доставка</a></li>
            </ul>
          </div>

          <div>
            <h4 style={{ marginBottom: '1rem', color: 'white' }}>Контакты</h4>
            <ul style={{ listStyle: 'none', fontSize: '0.875rem', lineHeight: 2 }}>
              <li style={{ opacity: 0.8 }}>Email: info@spavnit.com</li>
              <li style={{ opacity: 0.8 }}>Телефон: +7 (999) 123-45-67</li>
            </ul>
          </div>
        </div>

        <div style={{
          paddingTop: '1.5rem',
          borderTop: '1px solid rgba(255, 255, 255, 0.1)',
          textAlign: 'center',
          fontSize: '0.875rem',
          opacity: 0.8,
        }}>
          © 2026 SpavnIT Marketplace. Все права защищены.
        </div>
      </div>
    </footer>
  );
}