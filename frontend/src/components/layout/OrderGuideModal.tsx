import { X, ShoppingCart, CreditCard, Mail, ArrowDown } from 'lucide-react';

interface OrderGuideModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function OrderGuideModal({ isOpen, onClose }: OrderGuideModalProps) {
  if (!isOpen) return null;

  return (
    <>
      {/* Backdrop */}
      <div
        className="fixed inset-0 bg-black bg-opacity-50 z-40 transition-opacity"
        onClick={onClose}
        style={{
          animation: 'fadeIn 0.2s ease-out',
        }}
      />

      {/* Modal */}
      <div
        className="fixed inset-0 z-50 flex items-center justify-center p-4"
        onClick={onClose}
      >
        <div
          className="bg-white rounded-2xl shadow-2xl max-w-lg w-full p-6 relative"
          onClick={(e) => e.stopPropagation()}
          style={{
            animation: 'slideUp 0.3s ease-out',
          }}
        >
          {/* Close Button */}
          <button
            onClick={onClose}
            className="absolute top-4 right-4 text-gray-400 hover:text-gray-600 transition-colors"
            aria-label="Закрыть"
          >
            <X size={24} />
          </button>

          {/* Header */}
          <div className="mb-6">
            <h2 className="text-2xl font-bold text-gray-900 mb-2">
              Как сделать заказ?
            </h2>
            <p className="text-gray-600">
              Следуйте простым шагам для оформления заказа
            </p>
          </div>

          {/* Steps */}
          <div className="space-y-4">
            {/* Step 1 */}
            <div className="flex gap-4 items-start">
              <div
                className="flex-shrink-0 w-12 h-12 rounded-full flex items-center justify-center text-white font-bold"
                style={{ backgroundColor: 'var(--primary)' }}
              >
                1
              </div>
              <div className="flex-1 pt-2">
                <h3 className="font-semibold text-gray-900 mb-1 flex items-center gap-2">
                  <ShoppingCart size={20} className="text-blue-600" />
                  Выберите товары
                </h3>
                <p className="text-gray-600 text-sm">
                  Перейдите в каталог и добавьте нужные товары в корзину
                </p>
              </div>
            </div>

            <div className="flex justify-center">
              <ArrowDown size={20} className="text-gray-400" />
            </div>

            {/* Step 2 */}
            <div className="flex gap-4 items-start">
              <div
                className="flex-shrink-0 w-12 h-12 rounded-full flex items-center justify-center text-white font-bold"
                style={{ backgroundColor: 'var(--primary)' }}
              >
                2
              </div>
              <div className="flex-1 pt-2">
                <h3 className="font-semibold text-gray-900 mb-1 flex items-center gap-2">
                  <ShoppingCart size={20} className="text-green-600" />
                  Оформите заказ
                </h3>
                <p className="text-gray-600 text-sm">
                  Перейдите в корзину и нажмите кнопку "Оформить заказ"
                </p>
              </div>
            </div>

            <div className="flex justify-center">
              <ArrowDown size={20} className="text-gray-400" />
            </div>


            {/* Step 3 */}
            <div className="flex gap-4 items-start">
              <div
                className="flex-shrink-0 w-12 h-12 rounded-full flex items-center justify-center text-white font-bold"
                style={{ backgroundColor: 'var(--primary)' }}
              >
                3
              </div>
              <div className="flex-1 pt-2">
                <h3 className="font-semibold text-gray-900 mb-1 flex items-center gap-2">
                  <CreditCard size={20} className="text-purple-600" />
                  Оплатите заказ
                </h3>
                <p className="text-gray-600 text-sm">
                  Выберите удобный способ оплаты и завершите покупку
                </p>
              </div>
            </div>

            <div className="flex justify-center">
              <ArrowDown size={20} className="text-gray-400" />
            </div>

            {/* Step 4 */}
            <div className="flex gap-4 items-start">
              <div
                className="flex-shrink-0 w-12 h-12 rounded-full flex items-center justify-center text-white font-bold"
                style={{ backgroundColor: 'var(--primary)' }}
              >
                4
              </div>
              <div className="flex-1 pt-2">
                <h3 className="font-semibold text-gray-900 mb-1 flex items-center gap-2">
                  <Mail size={20} className="text-red-600" />
                  Проверьте почту
                </h3>
                <p className="text-gray-600 text-sm">
                  Ссылка на ваш товар придет на указанный email
                </p>
              </div>
            </div>
          </div>

          {/* Footer */}
          <div className="mt-6 pt-6 border-t border-gray-200">
            <button
              onClick={onClose}
              className="w-full py-3 px-4 rounded-lg font-semibold text-white transition-colors"
              style={{
                backgroundColor: 'var(--primary)',
              }}
              onMouseEnter={(e) => {
                e.currentTarget.style.backgroundColor = 'var(--primary-dark)';
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.backgroundColor = 'var(--primary)';
              }}
            >
              Понятно, спасибо!
            </button>
          </div>
        </div>
      </div>

      <style>{`
        @keyframes fadeIn {
          from {
            opacity: 0;
          }
          to {
            opacity: 1;
          }
        }

        @keyframes slideUp {
          from {
            opacity: 0;
            transform: translateY(20px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
      `}</style>
    </>
  );
}