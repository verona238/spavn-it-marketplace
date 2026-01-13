import { useState, useEffect } from 'react';
import { ShoppingCart, AlertCircle, RefreshCw } from 'lucide-react';
import { catalogService } from '../services/catalogService';
import ProductCard from '../components/features/ProductCard';

interface Product {
  id: number;
  name: string;
  price: number;
  description?: string;
  image?: string;
}

export default function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [addingToCart, setAddingToCart] = useState<number | null>(null);

  useEffect(() => {
    loadProducts();
  }, []);

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
    } catch (err: any) {
      console.error('Ошибка загрузки товаров:', err);
      setError(err.message || 'Не удалось загрузить товары');
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = async (product: Product) => {
    setAddingToCart(product.id);

    await new Promise(resolve => setTimeout(resolve, 500));

    console.log('Добавлен в корзину:', product);
    alert(`${product.name} добавлен в корзину!`);
    setAddingToCart(null);
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
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Каталог товаров</h1>
          <p className="text-gray-600">Найдено товаров: {products.length}</p>
        </div>
      </div>


      {/* Products Grid */}
      {products.length === 0 ? (
        <div className="text-center py-16 bg-gray-50 rounded-2xl">
          <ShoppingCart className="mx-auto text-gray-400 mb-4" size={64} />
          <h3 className="text-xl font-semibold text-gray-700 mb-2">
            Товары не найдены
          </h3>
          <p className="text-gray-500">
            Попробуйте изменить фильтры или вернитесь позже
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {products.map((product) => (
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