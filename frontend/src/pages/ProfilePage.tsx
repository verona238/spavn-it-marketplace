import { useState, useEffect } from 'react';
import { Package } from 'lucide-react';
import { userService } from '../services/userService';
import { balanceService } from '../services/balanceService';
import UserProfile from '../components/features/UserProfile';
import Card from '../components/ui/Card';

export default function ProfilePage() {
  const [user, setUser] = useState<any>(null);
  const [balance, setBalance] = useState<number>(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadUserData();
  }, []);

  const loadUserData = async () => {
    try {
      setLoading(true);
      const userId = 1;
      
      const [userData, balanceData] = await Promise.all([
        userService.getUserById(userId),
        balanceService.getUserBalance(userId),
      ]);
      
      setUser(userData);
      setBalance(balanceData.balance || 0);
    } catch (err) {
      console.error('Ошибка загрузки данных:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="container" style={{ textAlign: 'center', padding: '4rem 0' }}>
        <div className="loading" style={{
          width: '50px',
          height: '50px',
          borderWidth: '4px',
          borderColor: 'var(--primary)',
          borderTopColor: 'transparent',
          margin: '0 auto',
        }}></div>
      </div>
    );
  }

  return (
    <div className="container">
      <h1 style={{ marginBottom: '2rem' }}>Профиль</h1>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem' }}>
        <UserProfile
          user={{
            name: user?.name || 'Пользователь 1',
            email: user?.email || 'user1@example.com',
          }}
          balance={balance}
        />

        <Card>
          <h3 style={{ 
            marginBottom: '1.5rem',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
          }}>
            <Package size={24} />
            Мои заказы
          </h3>
          
          <div style={{
            textAlign: 'center',
            padding: '2rem',
            color: 'var(--text-secondary)',
          }}>
            <p>У вас пока нет заказов</p>
          </div>
        </Card>
      </div>
    </div>
  );
}