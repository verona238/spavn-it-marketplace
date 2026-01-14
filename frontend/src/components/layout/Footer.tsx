import { useState } from 'react';
import { Link } from 'react-router-dom';
import OrderGuideModal from './OrderGuideModal';

export default function Footer() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <>
      <footer style={{
        backgroundColor: '#1f2937',
        color: 'white',
        marginTop: 'auto',
        padding: '3rem 0 1.5rem',
      }}>
        <div className="container">
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
            gap: '2rem',
            marginBottom: '2rem',
          }}>
            {/* О компании */}
            <div>
              <h3 style={{
                fontSize: '1.2rem',
                fontWeight: 'bold',
                marginBottom: '1rem',
                color: 'white',
              }}>
                Спавн в IT
              </h3>
              <p style={{
                color: '#9ca3af',
                lineHeight: '1.6',
              }}>
                Всё для успешного старта и развития карьеры в IT-индустрии
              </p>
            </div>

            {/* Навигация */}
            <div>
              <h3 style={{
                fontSize: '1.2rem',
                fontWeight: 'bold',
                marginBottom: '1rem',
                color: 'white',
              }}>
                Навигация
              </h3>
              <ul style={{ listStyle: 'none', padding: 0 }}>
                <li style={{ marginBottom: '0.5rem' }}>
                  <Link
                    to="/products"
                    style={{
                      color: '#9ca3af',
                      textDecoration: 'none',
                      transition: 'color 0.2s',
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.color = 'white'}
                    onMouseLeave={(e) => e.currentTarget.style.color = '#9ca3af'}
                  >
                    Каталог товаров
                  </Link>
                </li>
                <li style={{ marginBottom: '0.5rem' }}>
                  <Link
                    to="/cart"
                    style={{
                      color: '#9ca3af',
                      textDecoration: 'none',
                      transition: 'color 0.2s',
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.color = 'white'}
                    onMouseLeave={(e) => e.currentTarget.style.color = '#9ca3af'}
                  >
                    Корзина
                  </Link>
                </li>
                <li style={{ marginBottom: '0.5rem' }}>
                  <Link
                    to="/profile"
                    style={{
                      color: '#9ca3af',
                      textDecoration: 'none',
                      transition: 'color 0.2s',
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.color = 'white'}
                    onMouseLeave={(e) => e.currentTarget.style.color = '#9ca3af'}
                  >
                    Профиль
                  </Link>
                </li>
              </ul>
            </div>


            {/* Помощь */}
            <div>
              <h3 style={{
                fontSize: '1.2rem',
                fontWeight: 'bold',
                marginBottom: '1rem',
                color: 'white',
              }}>
                Помощь
              </h3>
              <ul style={{ listStyle: 'none', padding: 0 }}>
                <li style={{ marginBottom: '0.5rem' }}>
                  <button
                    onClick={() => setIsModalOpen(true)}
                    style={{
                      color: '#9ca3af',
                      textDecoration: 'none',
                      transition: 'color 0.2s',
                      background: 'none',
                      border: 'none',
                      cursor: 'pointer',
                      padding: 0,
                      fontSize: '1rem',
                      textAlign: 'left',
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.color = 'white'}
                    onMouseLeave={(e) => e.currentTarget.style.color = '#9ca3af'}
                  >
                    Как сделать заказ
                  </button>
                </li>
                <li style={{ marginBottom: '0.5rem' }}>
                  <a
                    href="/cart"
                    style={{
                      color: '#9ca3af',
                      textDecoration: 'none',
                      transition: 'color 0.2s',
                    }}
                    onMouseEnter={(e) => e.currentTarget.style.color = 'white'}
                    onMouseLeave={(e) => e.currentTarget.style.color = '#9ca3af'}
                  >
                    Оплата
                  </a>
                </li>
              </ul>
            </div>

            {/* Контакты */}
            <div>
              <h3 style={{
                fontSize: '1.2rem',
                fontWeight: 'bold',
                marginBottom: '1rem',
                color: 'white',
              }}>
                Контакты
              </h3>
              <p style={{
                color: '#9ca3af',
                lineHeight: '1.6',
              }}>
                Email: nazarovskaya2380@mail.ru<br />
                Telegram: @vernonna
              </p>
            </div>
          </div>

          {/* Copyright */}
          <div style={{
            borderTop: '1px solid #374151',
            paddingTop: '1.5rem',
            textAlign: 'center',
            color: '#9ca3af',
            fontSize: '0.9rem',
          }}>
            © 2026 Спавн в IT. Все права защищены.
          </div>
        </div>
      </footer>

      {/* Modal */}
      <OrderGuideModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </>
  );
}