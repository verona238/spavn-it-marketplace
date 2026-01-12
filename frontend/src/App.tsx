import { useState } from 'react';
import { api } from './api/client';

function App() {
  const [response, setResponse] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const testConnection = async () => {
    setLoading(true);
    setError(null);
    setResponse(null);

    try {
      console.log('Начинаем тест подключения...');

      // ВАЖНО: Замените '/' на реальный эндпоинт вашего бэкенда
      // Например: '/api/test' или '/users' или '/health'
      const result = await api.get('/');

      console.log('Успешный ответ:', result);
      setResponse(JSON.stringify(result, null, 2));
    } catch (err) {
      console.error('Полная ошибка:', err);
      setError(err instanceof Error ? err.message : 'Неизвестная ошибка');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial, sans-serif' }}>
      <h1>🔌 Тест подключения к Backend</h1>

      <div style={{ marginBottom: '20px' }}>
        <p><strong>Проверьте:</strong></p>
        <ul>
          <li>✅ Бэкенд запущен на порту 8000?</li>
          <li>✅ CORS настроен на бэкенде?</li>
          <li>✅ Эндпоинт '/' существует на бэкенде?</li>
        </ul>
      </div>

      <button
        onClick={testConnection}
        disabled={loading}
        style={{
          padding: '10px 20px',
          fontSize: '16px',
          cursor: loading ? 'not-allowed' : 'pointer',
          backgroundColor: loading ? '#ccc' : '#007bff',
          color: 'white',
          border: 'none',
          borderRadius: '5px',
          marginBottom: '20px'
        }}
      >
        {loading ? '⏳ Загрузка...' : '🚀 Тестировать подключение'}
      </button>

      {error && (
        <div style={{
          padding: '15px',
          backgroundColor: '#f8d7da',
          color: '#721c24',
          borderRadius: '5px',
          border: '1px solid #f5c6cb'
        }}>
          <strong>❌ Ошибка:</strong> {error}
          <p style={{ marginTop: '10px', fontSize: '14px' }}>
            <strong>Возможные причины:</strong><br/>
            • Бэкенд не запущен<br/>
            • Неверный порт (проверьте, что бэкенд на порту 8000)<br/>
            • Проблема с CORS<br/>
            • Эндпоинт не существует
          </p>
        </div>
      )}

      {response && (
        <div style={{
          padding: '15px',
          backgroundColor: '#d4edda',
          color: '#155724',
          borderRadius: '5px',
          border: '1px solid #c3e6cb'
        }}>
          <strong>✅ Успешный ответ:</strong>
          <pre style={{
            marginTop: '10px',
            overflow: 'auto',
            backgroundColor: 'white',
            padding: '10px',
            borderRadius: '3px'
          }}>
            {response}
          </pre>
        </div>
      )}
    </div>
  );
}

export default App;