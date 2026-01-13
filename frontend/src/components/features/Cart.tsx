import { Trash2, Plus, Minus } from 'lucide-react';
import Card from '../ui/Card';
import Button from '../ui/Button';

interface CartItemData {
  id: number;
  productId: number;
  name: string;
  quantity: number;
  price: number;
}

interface CartItemProps {
  item: CartItemData;
  onRemove: (id: number) => void;
  onUpdateQuantity: (id: number, quantity: number) => void;
}

export function CartItem({ item, onRemove, onUpdateQuantity }: CartItemProps) {
  return (
    <Card>
      <div style={{
        display: 'flex',
        alignItems: 'center',
        gap: '1.5rem',
      }}>
        <div style={{
          width: '80px',
          height: '80px',
          backgroundColor: 'var(--surface)',
          borderRadius: 'var(--radius-md)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          fontSize: '2rem',
          flexShrink: 0,
        }}>
          📦
        </div>

        <div style={{ flex: 1 }}>
          <h3 style={{ marginBottom: '0.25rem', fontSize: '1.125rem' }}>
            {item.name}
          </h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
            {item.price} ₽
          </p>
        </div>

        {/* Quantity controls */}
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '0.5rem',
          padding: '0.5rem',
          backgroundColor: 'var(--surface)',
          borderRadius: 'var(--radius-md)',
        }}>
          <button
            onClick={() => onUpdateQuantity(item.id, Math.max(1, item.quantity - 1))}
            style={{
              padding: '0.25rem',
              backgroundColor: 'transparent',
              border: 'none',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              color: 'var(--text-secondary)',
            }}
          >
            <Minus size={16} />
          </button>

          <span style={{
            minWidth: '2rem',
            textAlign: 'center',
            fontWeight: 600,
          }}>
            {item.quantity}
          </span>

          <button
            onClick={() => onUpdateQuantity(item.id, item.quantity + 1)}
            style={{
              padding: '0.25rem',
              backgroundColor: 'transparent',
              border: 'none',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              color: 'var(--text-secondary)',
            }}
          >
            <Plus size={16} />
          </button>
        </div>

        <div style={{
          fontSize: '1.25rem',
          fontWeight: 700,
          color: 'var(--primary)',
          minWidth: '100px',
          textAlign: 'right',
        }}>
          {item.price * item.quantity} ₽
        </div>

        <Button
          variant="secondary"
          size="sm"
          onClick={() => onRemove(item.id)}
          style={{ color: 'var(--error)' }}
        >
          <Trash2 size={18} />
        </Button>
      </div>
    </Card>
  );
}

// Экспорт по умолчанию для совместимости
export default CartItem;