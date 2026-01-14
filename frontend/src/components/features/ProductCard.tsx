import { useState } from 'react';
import { ShoppingCart, X } from 'lucide-react';

interface Product {
  id: number;
  name: string;
  price: number;
  description?: string;
  image?: string;
  category?: string;
}

interface ProductCardProps {
  product: Product;
  onAddToCart: (product: Product) => void;
  loading?: boolean;
}

export default function ProductCard({ product, onAddToCart, loading = false }: ProductCardProps) {
  const [showModal, setShowModal] = useState(false);

  // Функция для сокращения описания
  const truncateDescription = (text: string, maxLength: number = 80) => {
    if (!text) return '';
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength).trim() + '...';
  };

  // Модальное окно
  const ProductModal = () => (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
      onClick={() => setShowModal(false)}
    >
      <div
        className="bg-white rounded-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex justify-between items-center">
          <h2 className="text-2xl font-bold text-gray-900">{product.name}</h2>
          <button
            onClick={() => setShowModal(false)}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X size={24} />
          </button>
        </div>

        <div className="p-6">
          {/* Image */}
          <div className="bg-gray-100 rounded-lg mb-6 overflow-hidden">
            {product.image ? (
              <img
                src={product.image}
                alt={product.name}
                className="w-full h-96 object-cover"
              />
            ) : (
              <div className="w-full h-96 flex items-center justify-center text-8xl">
                📦
              </div>
            )}
          </div>

          {/* Category */}
          {product.category && (
            <div className="mb-4">
              <span className="inline-block px-3 py-1 text-sm bg-blue-100 text-blue-800 rounded-full">
                {product.category}
              </span>
            </div>
          )}

          {/* Description */}
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-2">Описание</h3>
            <p className="text-gray-700 leading-relaxed">
              {product.description || 'Описание отсутствует'}
            </p>
          </div>

          {/* Price and Action */}
          <div className="flex items-center justify-between pt-6 border-t border-gray-200">
            <span className="text-3xl font-bold text-blue-600">
              {product.price} ₽
            </span>
            <button
              onClick={() => {
                onAddToCart(product);
                setShowModal(false);
              }}
              disabled={loading}
              className="btn btn-primary px-8"
            >
              {loading ? (
                <div className="loading-spinner" />
              ) : (
                <>
                  <ShoppingCart size={20} />
                  Добавить в корзину
                </>
              )}
            </button>
          </div>
        </div>
      </div>
    </div>
  );

  return (
    <>
      <div className="card card-hover h-full flex flex-col overflow-hidden">
        {/* Image - кликабельна */}
        <div
          className="bg-gray-100 h-48 flex items-center justify-center overflow-hidden cursor-pointer"
          onClick={() => setShowModal(true)}
        >
          {product.image ? (
            <img
              src={product.image}
              alt={product.name}
              className="w-full h-full object-cover transition-transform duration-300 hover:scale-110"
            />
          ) : (
            <div className="text-6xl">📦</div>
          )}
        </div>


        {/* Content */}
        <div className="p-4 flex flex-col flex-1">
          <h3
            className="text-lg font-semibold text-gray-900 mb-2 cursor-pointer hover:text-blue-600 transition-colors"
            onClick={() => setShowModal(true)}
          >
            {product.name}
          </h3>

          {product.description && (
            <p className="text-sm text-gray-600 mb-4 flex-1">
              {truncateDescription(product.description)}
            </p>
          )}

          {/* Category badge */}
          {product.category && (
            <div className="mb-3">
              <span className="inline-block px-2 py-1 text-xs bg-gray-100 text-gray-700 rounded">
                {product.category}
              </span>
            </div>
          )}

          {/* Footer */}
          <div className="flex items-center justify-between mt-auto pt-4 border-t border-gray-100">
            <span className="text-xl font-bold text-blue-600">
              {product.price} ₽
            </span>

            <button
              onClick={() => onAddToCart(product)}
              disabled={loading}
              className="btn btn-primary text-sm py-2"
            >
              {loading ? (
                <div className="loading-spinner" />
              ) : (
                <>
                  <ShoppingCart size={16} />
                  В корзину
                </>
              )}
            </button>
          </div>
        </div>
      </div>

      {/* Modal */}
      {showModal && <ProductModal />}
    </>
  );
}