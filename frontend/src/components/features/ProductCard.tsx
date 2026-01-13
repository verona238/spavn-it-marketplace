import { ShoppingCart } from 'lucide-react';

interface Product {
  id: number;
  name: string;
  price: number;
  description?: string;
  image?: string;
}

interface ProductCardProps {
  product: Product;
  onAddToCart: (product: Product) => void;
  loading?: boolean;
}

export default function ProductCard({ product, onAddToCart, loading = false }: ProductCardProps) {
  return (
    <div className="card card-hover p-4 h-full flex flex-col">
      {/* Image */}
      <div className="bg-gray-100 rounded-lg h-48 flex items-center justify-center mb-4 text-5xl">
        📦
      </div>

      {/* Content */}
      <h3 className="text-lg font-semibold text-gray-900 mb-2">
        {product.name}
      </h3>

      {product.description && (
        <p className="text-sm text-gray-600 mb-4 flex-1 line-clamp-2">
          {product.description}
        </p>
      )}

      {/* Footer */}
      <div className="flex items-center justify-between mt-auto pt-4 border-t border-gray-100">
        <span className="text-2xl font-bold text-blue-600">
          {product.price} ₽
        </span>

        <button
          onClick={() => onAddToCart(product)}
          disabled={loading}
          className="btn btn-primary"
        >
          {loading ? (
            <div className="loading-spinner" />
          ) : (
            <>
              <ShoppingCart size={18} />
              В корзину
            </>
          )}
        </button>
      </div>
    </div>
  );
}