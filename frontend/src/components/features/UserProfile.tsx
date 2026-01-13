import { User, Mail, Wallet } from 'lucide-react';
import Card from '../ui/Card';
import Badge from '../ui/Badge';

interface UserProfileProps {
  user: {
    name: string;
    email: string;
  };
  balance: number;
}

export default function UserProfile({ user, balance }: UserProfileProps) {
  return (
    <Card>
      <div style={{
        width: '80px',
        height: '80px',
        borderRadius: '50%',
        backgroundColor: 'var(--primary-light)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        margin: '0 auto 1.5rem',
        color: 'var(--primary)',
      }}>
        <User size={40} />
      </div>

      <h2 style={{ textAlign: 'center', marginBottom: '1.5rem' }}>
        {user.name}
      </h2>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '0.75rem',
          padding: '0.75rem',
          backgroundColor: 'var(--surface)',
          borderRadius: 'var(--radius-md)',
        }}>
          <Mail size={20} style={{ color: 'var(--text-secondary)' }} />
          <span style={{ fontSize: '0.875rem' }}>{user.email}</span>
        </div>

        <div style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          padding: '0.75rem',
          backgroundColor: 'var(--surface)',
          borderRadius: 'var(--radius-md)',
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
            <Wallet size={20} style={{ color: 'var(--text-secondary)' }} />
            <span style={{ fontSize: '0.875rem' }}>Баланс</span>
          </div>
          <Badge variant="success">
            {balance} ₽
          </Badge>
        </div>
      </div>
    </Card>
  );
}