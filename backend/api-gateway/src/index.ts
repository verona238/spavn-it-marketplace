import express, { Request, Response } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 8000;

// Получаем URL сервисов из .env
const AUTH_SERVICE_URL = process.env.AUTH_SERVICE_URL || 'http://localhost:8081';
const CATALOG_SERVICE_URL = process.env.CATALOG_SERVICE_URL || 'http://localhost:8085';
const CART_SERVICE_URL = process.env.CART_SERVICE_URL || 'http://localhost:8086';
const ORDER_SERVICE_URL = process.env.ORDER_SERVICE_URL || 'http://localhost:8087';
const USER_SERVICE_URL = process.env.USER_SERVICE_URL || 'http://localhost:8083';
const BALANCE_SERVICE_URL = process.env.BALANCE_SERVICE_URL || 'http://localhost:8084';
const NOTIFICATION_SERVICE_URL = process.env.NOTIFICATION_SERVICE_URL || 'http://localhost:8082';

// CORS
const allowedOrigins = process.env.ALLOWED_ORIGINS?.split(',') || ['http://localhost:5173'];

app.use(cors({
  origin: allowedOrigins,
  credentials: true,
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS', 'PATCH'],
  allowedHeaders: ['Content-Type', 'Authorization', 'X-Requested-With']
}));

app.use(express.json());

// Логирование
app.use((req, res, next) => {
  console.log(`${new Date().toISOString()} - ${req.method} ${req.path}`);
  next();
});

// Корневой эндпоинт
app.get('/', (req: Request, res: Response) => {
  res.json({
    message: 'SpavnIT Marketplace API Gateway',
    version: '1.0.0',
    environment: process.env.NODE_ENV || 'development',
    services: {
      auth: AUTH_SERVICE_URL,
      catalog: CATALOG_SERVICE_URL,
      cart: CART_SERVICE_URL,
      order: ORDER_SERVICE_URL,
      user: USER_SERVICE_URL,
      balance: BALANCE_SERVICE_URL,
      notification: NOTIFICATION_SERVICE_URL
    }
  });
});

// Health check
app.get('/api/health', (req: Request, res: Response) => {
  res.json({
    status: 'OK',
    message: 'API Gateway работает!',
    timestamp: new Date().toISOString()
  });
});

// Helper function для проксирования
async function proxyRequest(serviceUrl: string, servicePath: string, req: Request, res: Response) {
  try {
    const url = `${serviceUrl}${servicePath}`;
    console.log(`Proxying ${req.method} to: ${url}`);

    const response = await fetch(url, {
      method: req.method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': req.headers.authorization || ''
      },
      body: req.method !== 'GET' && req.method !== 'HEAD' ? JSON.stringify(req.body) : undefined
    });

    const contentType = response.headers.get('content-type');

    if (contentType && contentType.includes('application/json')) {
      const data = await response.json();
      res.status(response.status).json(data);
    } else {
      const text = await response.text();
      res.status(response.status).send(text);
    }
  } catch (error) {
    console.error('Service error:', error);
    res.status(503).json({
      error: 'Service unavailable',
      message: error instanceof Error ? error.message : 'Unknown error',
      service: serviceUrl
    });
  }
}

// Auth Service (8081)
app.all('/api/auth*', (req: Request, res: Response) => {
  const path = req.path.replace('/api/auth', '/auth');
  proxyRequest(AUTH_SERVICE_URL, path, req, res);
});

// Notification Service (8082)
app.all('/api/notifications*', (req: Request, res: Response) => {
  const path = req.path.replace('/api/notifications', '/notifications');
  proxyRequest(NOTIFICATION_SERVICE_URL, path, req, res);
});

// User Service (8083)
app.all('/api/users*', (req: Request, res: Response) => {
  const path = req.path.replace('/api/users', '/users');
  proxyRequest(USER_SERVICE_URL, path, req, res);
});

// Balance Service (8084)
app.all('/api/balance*', (req: Request, res: Response) => {
  const path = req.path.replace('/api/balance', '/balance');
  proxyRequest(BALANCE_SERVICE_URL, path, req, res);
});



// Catalog Service / Products (8085)
app.all('/api/products*', (req: Request, res: Response) => {
  const path = req.path.replace('/api/products', '/products');
  proxyRequest(CATALOG_SERVICE_URL, path, req, res);
});

app.all('/api/catalog*', (req: Request, res: Response) => {
  const path = req.path.replace('/api/catalog', '/catalog');
  proxyRequest(CATALOG_SERVICE_URL, path, req, res);
});

// Cart Service (8086)
app.all('/api/cart*', (req: Request, res: Response) => {
  const path = req.path.replace('/api/cart', '/cart');
  proxyRequest(CART_SERVICE_URL, path, req, res);
});

// Order Service (8087)
app.all('/api/orders*', (req: Request, res: Response) => {
  const path = req.path.replace('/api/orders', '/orders');
  proxyRequest(ORDER_SERVICE_URL, path, req, res);
});

app.listen(PORT, () => {
  console.log(`
╔═══════════════════════════════════════════════════╗
║  🚀 SpavnIT Marketplace API Gateway              ║
╚═══════════════════════════════════════════════════╝

📍 Server:     http://localhost:${PORT}
🌍 Environment: ${process.env.NODE_ENV || 'development'}

📡 Микросервисы:
   🔐 Auth:          ${AUTH_SERVICE_URL}
   🔔 Notification:  ${NOTIFICATION_SERVICE_URL}
   👤 User:          ${USER_SERVICE_URL}
   💰 Balance:       ${BALANCE_SERVICE_URL}
   📦 Catalog:       ${CATALOG_SERVICE_URL}
   🛒 Cart:          ${CART_SERVICE_URL}
   📋 Order:         ${ORDER_SERVICE_URL}

🌐 CORS разрешён для: ${allowedOrigins.join(', ')}
  `);
});