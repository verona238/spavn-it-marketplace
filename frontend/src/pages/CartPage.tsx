import { useState } from 'react';
import { ShoppingBag } from 'lucide-react';
import { CartItem } from '../components/features/Cart';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';

interface CartItemData {
  id: number;
  productId: number;
  name: string;
  quantity: number;
  price: number;
}

export default function CartPage() {
  const [cartItems, setCartItems] = useState<CartItemData[]>([
    { id: 1, productId: 1, name: 'Товар 1', quantity: 2, price: 100 },
    { id: 2, productId: 2, name: 'Товар 2', quantity: 1, price: 200 },
  ]);

  const handleRemove = (id: number) => {
    setCartItems(items => items.filter(item => item.id !== id));
  };

  const handleUpdateQuantity = (id: number, quantity: number) => {
    setCartItems(items =>
      items.map(item => (item.id === id ? { ...item, quantity } : item))
    );
  };

  const total = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

  return (
    <div className="container">
      <h1 style={{ marginBottom: '2rem' }}>Корзина</h1>

      {cartItems.length === 0 ? (
        <div style={{
          textAlign: 'center',
          padding: '4rem 2rem',
          backgroundColor: 'var(--surface)',
          borderRadius: 'var(--radius-lg)',
        }}>
          <ShoppingBag size={64} style={{
            color: 'var(--text-secondary)',
            opacity: 0.5,
            margin: '0 auto 1rem',
          }} />
          <h3 style={{ marginBottom: '0.5rem', color: 'var(--text-secondary)' }}>
            Корзина пуста
          </h3>
          <p style={{ color: 'var(--text-secondary)' }}>
            Добавьте товары из каталога
          </p>
        </div>
      ) : (
        <div style={{
          display: 'grid',
          gridTemplateColumns: '1fr 350px',
          gap: '2rem',
          '@media (max-width: 768px)': {
            gridTemplateColumns: '1fr',
          }
        }}>
          {/* Cart Items */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {cartItems.map((item) => (
              <CartItem
                key={item.id}
                item={item}
                onRemove={handleRemove}
                onUpdateQuantity={handleUpdateQuantity}
              />
            ))}
          </div>

          {/* Order Summary */}
          <div>
            <Card style={{ position: 'sticky', top: '5rem' }}>
              <h3 style={{ marginBottom: '1.5rem' }}>Итого</h3>

              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                marginBottom: '1rem',
                paddingBottom: '1rem',
                borderBottom: '1px solid var(--border)',
              }}>
                <span style={{ color: 'var(--text-secondary)' }}>
                  Товары ({cartItems.length})
                </span>
                <span style={{ fontWeight: 600 }}>
                  {total} ₽
                </span>
              </div>

              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                marginBottom: '1.5rem',
                fontSize: '1.25rem',
                fontWeight: 700,
              }}>
                <span>Итого:</span>
                <span style={{ color: 'var(--primary)' }}>
                  {total} ₽
                </span>
              </div>

              <Button
                variant="primary"
                style={{ width: '100%', padding: '1rem', fontSize: '1rem' }}
              >
                Оформить заказ
              </Button>
            </Card>
          </div>
        </div>
      )}
    </div>
  );
}