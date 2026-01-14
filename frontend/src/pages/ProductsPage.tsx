import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { ShoppingCart, AlertCircle, RefreshCw, Search } from 'lucide-react';
import { catalogService } from '../services/catalogService';
import ProductCard from '../components/features/ProductCard';
import { getCategoryDisplayName } from '../constants/categories';

interface Product {
  id: number;
  name: string;
  price: number;
  description?: string;
  image?: string;
  category?: string;
}

export default function ProductsPage() {
  const location = useLocation();

  const [products, setProducts] = useState<Product[]>([]);
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [addingToCart, setAddingToCart] = useState<number | null>(null);

  // Фильтры
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [categories, setCategories] = useState<string[]>([]);

  useEffect(() => {
    loadProducts();
  }, []);

  // Устанавливаем категорию из navigation state
  useEffect(() => {
    if (location.state?.selectedCategory) {
      console.log('Выбранная категория:', location.state.selectedCategory);
      setSelectedCategory(location.state.selectedCategory);
      // Очищаем state
      window.history.replaceState({}, document.title);
    }
  }, [location.state]);

  useEffect(() => {
    filterProducts();
  }, [products, searchQuery, selectedCategory]);

  const loadProducts = async () => {
    try {
      setLoading(true);
      setError(null);

      console.log('Загрузка товаров...');
      const response = await catalogService.getAllProducts();
      console.log('Ответ от сервера:', response);

      // Обрабатываем разные форматы ответа
      let productsList = [];
      if (Array.isArray(response)) {
        productsList = response;
      } else if (response.products && Array.isArray(response.products)) {
        productsList = response.products;
      } else if (response.data && Array.isArray(response.data)) {
        productsList = response.data;
      }

      console.log('Список товаров:', productsList);
      setProducts(productsList);

      // Извлекаем уникальные категории (английские ключи)
      const uniqueCategories = [...new Set(productsList.map((p: Product) => p.category).filter(Boolean))];
      console.log('Доступные категории:', uniqueCategories);
      setCategories(uniqueCategories as string[]);

    } catch (err: any) {
      console.error('Ошибка загрузки товаров:', err);
      setError(err.message || 'Не удалось загрузить товары');
    } finally {
      setLoading(false);
    }
  };

  const filterProducts = () => {
    let filtered = [...products];

    // Фильтр по поиску
    if (searchQuery) {
      filtered = filtered.filter(product =>
        product.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        (product.description && product.description.toLowerCase().includes(searchQuery.toLowerCase()))
      );
    }

    // Фильтр по категории
    if (selectedCategory !== 'all') {
      console.log('Фильтрация по категории:', selectedCategory);
      filtered = filtered.filter(product => {
        console.log('Товар:', product.name, 'Категория:', product.category);
        return product.category === selectedCategory;
      });
    }

    console.log('Отфильтрованные товары:', filtered);
    setFilteredProducts(filtered);
  };

  const handleAddToCart = async (product: Product) => {
    try {
      setAddingToCart(product.id);

      const { cartService } = await import('../services/cartService');
      await cartService.addToCart(product.id, 1);

      console.log('Товар успешно добавлен в корзину:', product);
      alert(`${product.name} добавлен в корзину!`);
    } catch (error: any) {
      console.error('Ошибка добавления в корзину:', error);


      if (error.message.includes('авторизация')) {
        alert('Пожалуйста, войдите в систему для добавления товаров в корзину');
      } else {
        alert(`Ошибка: ${error.message || 'Не удалось добавить товар в корзину'}`);
      }
    } finally {
      setAddingToCart(null);
    }
  };

  if (loading) {
    return (
      <div className="container py-16 text-center">
        <div className="loading-spinner w-12 h-12 mx-auto border-4"></div>
        <p className="mt-4 text-gray-600">Загрузка товаров...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container py-16">
        <div className="max-w-md mx-auto bg-red-50 border border-red-200 rounded-xl p-6">
          <div className="flex items-start gap-3">
            <AlertCircle className="text-red-600 flex-shrink-0 mt-0.5" size={24} />
            <div className="flex-1">
              <h3 className="font-semibold text-red-900 mb-1">Ошибка загрузки</h3>
              <p className="text-red-700 text-sm mb-4">{error}</p>
              <button
                onClick={loadProducts}
                className="btn btn-secondary text-sm"
              >
                <RefreshCw size={16} />
                Попробовать снова
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container py-8">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Каталог товаров</h1>
        <p className="text-gray-600">Найдено товаров: {filteredProducts.length}</p>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-4 mb-6">
        <div className="flex flex-col lg:flex-row gap-4">
          {/* Search */}
          <div className="flex-1">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
              <input
                type="text"
                placeholder="Поиск по названию или описанию..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
          </div>

          {/* Category Filter */}
          <div className="lg:w-64">
            <select
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white"
            >
              <option value="all">Все категории</option>
              {categories.map((category) => (
                <option key={category} value={category}>
                  {getCategoryDisplayName(category)}
                </option>
              ))}
            </select>
          </div>

          {/* Reset Button */}
          {(searchQuery || selectedCategory !== 'all') && (
            <button
              onClick={() => {
                setSearchQuery('');
                setSelectedCategory('all');
              }}
              className="btn btn-secondary whitespace-nowrap"
            >
              Сбросить фильтры
            </button>
          )}
        </div>
      </div>


      {/* Products Grid */}
      {filteredProducts.length === 0 ? (
        <div className="text-center py-16 bg-gray-50 rounded-2xl">
          <ShoppingCart className="mx-auto text-gray-400 mb-4" size={64} />
          <h3 className="text-xl font-semibold text-gray-700 mb-2">
            {searchQuery || selectedCategory !== 'all'
              ? 'Товары не найдены'
              : 'Каталог пуст'}
          </h3>
          <p className="text-gray-500">
            {searchQuery || selectedCategory !== 'all'
              ? 'Попробуйте изменить фильтры или поисковый запрос'
              : 'Товары появятся позже'}
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {filteredProducts.map((product) => (
            <ProductCard
              key={product.id}
              product={product}
              onAddToCart={handleAddToCart}
              loading={addingToCart === product.id}
            />
          ))}
        </div>
      )}
    </div>
  );
}