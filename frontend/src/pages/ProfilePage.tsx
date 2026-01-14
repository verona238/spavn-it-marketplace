import { useState, useEffect } from 'react';
import { Package, User, Mail, Wallet, Settings, LogOut } from 'lucide-react';
import { userService } from '../services/userService';
import { balanceService } from '../services/balanceService';
import { orderService } from '../services/orderService';

interface UserData {
  id: number;
  email: string;
  firstName?: string;
  lastName?: string;
  role?: string;
}

interface Order {
  id: number;
  totalAmount: number;
  status: string;
  createdAt: string;
}

export default function ProfilePage() {
  const [user, setUser] = useState<UserData | null>(null);
  const [balance, setBalance] = useState<number>(0);
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadUserData();
  }, []);

  const loadUserData = async () => {
    try {
      setLoading(true);
      setError(null);

      const token = localStorage.getItem('token');
      if (!token) {
        setError('Необходима авторизация');
        return;
      }

      // Загружаем данные параллельно
      const [userData, balanceData, ordersData] = await Promise.allSettled([
        userService.getCurrentUser(),
        balanceService.getMyBalance(),
        orderService.getMyOrders(),
      ]);

      if (userData.status === 'fulfilled') {
        setUser(userData.value);
      }

      if (balanceData.status === 'fulfilled') {
        setBalance(balanceData.value?.balance || 0);
      }

      if (ordersData.status === 'fulfilled') {
        setOrders(ordersData.value || []);
      }

    } catch (err: any) {
      console.error('Ошибка загрузки данных профиля:', err);
      setError(err.message || 'Не удалось загрузить данные');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    window.location.href = '/login';
  };

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      'PENDING': 'bg-yellow-100 text-yellow-800',
      'PAID': 'bg-green-100 text-green-800',
      'CANCELLED': 'bg-red-100 text-red-800',
      'COMPLETED': 'bg-blue-100 text-blue-800',
    };
    return colors[status] || 'bg-gray-100 text-gray-800';
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  if (loading) {
    return (
      <div className="container py-16 text-center">
        <div className="loading-spinner w-12 h-12 mx-auto border-4"></div>
        <p className="mt-4 text-gray-600">Загрузка профиля...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container py-16">
        <div className="max-w-md mx-auto bg-red-50 border border-red-200 rounded-xl p-6 text-center">
          <p className="text-red-800">{error}</p>
          <button
            onClick={() => window.location.href = '/login'}
            className="mt-4 btn btn-primary"
          >
            Войти
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="container py-8">
      {/* Header */}
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Мой профиль</h1>
        <button
          onClick={handleLogout}
          className="btn btn-secondary text-sm"
        >
          <LogOut size={16} />
          Выйти
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* User Info Card */}
        <div className="lg:col-span-1">
          <div className="card p-6">
            <div className="flex items-center justify-center mb-6">
              <div className="w-20 h-20 bg-blue-100 rounded-full flex items-center justify-center">
                <User className="text-blue-600" size={40} />
              </div>
            </div>



            <div className="space-y-4">
              <div>
                <label className="text-sm text-gray-500 block mb-1">Имя</label>
                <p className="text-lg font-semibold text-gray-900">
                  {user?.firstName || user?.lastName
                    ? `${user.firstName || ''} ${user.lastName || ''}`.trim()
                    : 'Не указано'}
                </p>
              </div>

              <div>
                <label className="text-sm text-gray-500 block mb-1 flex items-center gap-2">
                  <Mail size={16} />
                  Email
                </label>
                <p className="text-gray-900">{user?.email}</p>
              </div>

              {user?.role && (
                <div>
                  <label className="text-sm text-gray-500 block mb-1">Роль</label>
                  <span className="inline-block px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm">
                    {user.role}
                  </span>
                </div>
              )}

              <div className="pt-4 border-t border-gray-200">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2 text-gray-700">
                    <Wallet size={20} />
                    <span className="text-sm">Баланс</span>
                  </div>
                  <span className="text-2xl font-bold text-green-600">
                    {balance.toFixed(2)} ₽
                  </span>
                </div>
              </div>

              <button className="w-full btn btn-secondary mt-4">
                <Settings size={18} />
                Редактировать профиль
              </button>
            </div>
          </div>
        </div>

        {/* Orders Card */}
        <div className="lg:col-span-2">
          <div className="card p-6">
            <div className="flex items-center gap-3 mb-6">
              <Package size={28} className="text-blue-600" />
              <h2 className="text-2xl font-bold text-gray-900">Мои заказы</h2>
            </div>



            {orders.length === 0 ? (
              <div className="text-center py-12 bg-gray-50 rounded-xl">
                <Package className="mx-auto text-gray-400 mb-4" size={64} />
                <h3 className="text-lg font-semibold text-gray-700 mb-2">
                  У вас пока нет заказов
                </h3>
                <p className="text-gray-500 mb-6">
                  Начните делать покупки в нашем каталоге
                </p>
                <a href="/products" className="btn btn-primary">
                  Перейти к покупкам
                </a>
              </div>
            ) : (
              <div className="space-y-4">
                {orders.map((order) => (
                  <div
                    key={order.id}
                    className="border border-gray-200 rounded-lg p-4 hover:border-blue-300 transition-colors"
                  >
                    <div className="flex justify-between items-start mb-3">
                      <div>
                        <p className="font-semibold text-gray-900">
                          Заказ #{order.id}
                        </p>
                        <p className="text-sm text-gray-500">
                          {formatDate(order.createdAt)}
                        </p>
                      </div>
                      <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}>
                        {order.status}
                      </span>
                    </div>
                    <div className="flex justify-between items-center pt-3 border-t border-gray-100">
                      <span className="text-gray-600">Сумма заказа:</span>
                      <span className="text-xl font-bold text-gray-900">
                        {order.totalAmount.toFixed(2)} ₽
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}