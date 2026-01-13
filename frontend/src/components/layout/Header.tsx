import { Link, useLocation } from 'react-router-dom';
import { ShoppingCart, User, Package, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export default function Header() {
  const location = useLocation();
  const { user, logout } = useAuth();

  const isActive = (path: string) => location.pathname === path;

  return (
    <header className="bg-white border-b border-gray-200 sticky top-0 z-50 shadow-sm">
      <div className="container">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-2 text-blue-600 text-xl font-bold hover:text-blue-700 transition-colors">
            <Package size={28} />
            SpavnIT
          </Link>

          {/* Navigation */}
          <nav className="flex items-center gap-6">
            <Link
              to="/"
              className={`font-medium transition-colors ${
                isActive('/') ? 'text-blue-600 border-b-2 border-blue-600' : 'text-gray-600 hover:text-gray-900'
              } pb-1`}
            >
              Главная
            </Link>

            <Link
              to="/products"
              className={`font-medium transition-colors ${
                isActive('/products') ? 'text-blue-600 border-b-2 border-blue-600' : 'text-gray-600 hover:text-gray-900'
              } pb-1`}
            >
              Товары
            </Link>

            <Link
              to="/cart"
              className={`flex items-center gap-2 px-3 py-2 rounded-lg font-medium transition-all ${
                isActive('/cart')
                  ? 'bg-blue-50 text-blue-600'
                  : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              <ShoppingCart size={20} />
              Корзина
            </Link>

            <Link
              to="/profile"
              className={`flex items-center gap-2 px-3 py-2 rounded-lg font-medium transition-all ${
                isActive('/profile')
                  ? 'bg-blue-50 text-blue-600'
                  : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              <User size={20} />
              {user?.name || 'Профиль'}
            </Link>

            <button
              onClick={logout}
              className="flex items-center gap-2 px-3 py-2 rounded-lg font-medium text-red-600 hover:bg-red-50 transition-all"
            >
              <LogOut size={20} />
              Выйти
            </button>
          </nav>
        </div>
      </div>
    </header>
  );
}