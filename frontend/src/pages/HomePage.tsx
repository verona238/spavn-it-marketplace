import { Link } from 'react-router-dom';
import { Package, ShoppingCart, Truck, Shield } from 'lucide-react';
import Button from '../components/ui/Button';

export default function HomePage() {
  return (
    <div>
      {/* Hero Section */}
      <section style={{
        background: 'linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%)',
        color: 'white',
        padding: '4rem 0',
        marginBottom: '4rem',
      }}>
        <div className="container" style={{ textAlign: 'center' }}>
          <h1 style={{
            fontSize: '3rem',
            marginBottom: '1rem',
            color: 'white',
          }}>
            Добро пожаловать в SpavnIT Marketplace
          </h1>
          <p style={{
            fontSize: '1.25rem',
            marginBottom: '2rem',
            opacity: 0.9,
            maxWidth: '600px',
            margin: '0 auto 2rem',
          }}>
            Современный маркетплейс с широким ассортиментом товаров
          </p>
          <Link to="/products">
            <Button
              variant="primary"
              size="lg"
              style={{
                backgroundColor: 'white',
                color: 'var(--primary)',
              }}
            >
              <Package size={20} />
              Посмотреть товары
            </Button>
          </Link>
        </div>
      </section>

      {/* Features */}
      <section className="container" style={{ marginBottom: '4rem' }}>
        <h2 style={{ textAlign: 'center', marginBottom: '3rem' }}>
          Почему выбирают нас
        </h2>

        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
          gap: '2rem',
        }}>
          <div style={{ textAlign: 'center' }}>
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              backgroundColor: 'var(--primary-light)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1rem',
              color: 'var(--primary)',
            }}>
              <ShoppingCart size={36} />
            </div>
            <h3 style={{ marginBottom: '0.5rem' }}>Удобная корзина</h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
              Простое добавление товаров и оформление заказов
            </p>
          </div>

          <div style={{ textAlign: 'center' }}>
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              backgroundColor: '#d1fae5',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1rem',
              color: 'var(--success)',
            }}>
              <Truck size={36} />
            </div>
            <h3 style={{ marginBottom: '0.5rem' }}>Быстрая доставка</h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
              Доставляем заказы в кратчайшие сроки
            </p>
          </div>

          <div style={{ textAlign: 'center' }}>
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              backgroundColor: '#fef3c7',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1rem',
              color: 'var(--warning)',
            }}>
              <Shield size={36} />
            </div>
            <h3 style={{ marginBottom: '0.5rem' }}>Безопасность</h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
              Защита данных и безопасные платежи
            </p>
          </div>
        </div>
      </section>
    </div>
  );
}