import express, { Request, Response, NextFunction } from 'express';
import cors from 'cors';
import helmet from 'helmet';
import rateLimit from 'express-rate-limit';
import { createProxyMiddleware } from 'http-proxy-middleware';
import dotenv from 'dotenv';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

// ============================================
// MIDDLEWARE
// ============================================

app.use(helmet());
app.use(cors({
  origin: process.env.ALLOWED_ORIGINS?.split(',') || ['http://localhost:5173'],
  credentials: true
}));
app.use(express.json());

// Rate limiting
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000,
  max: 100
});
app.use('/api/', limiter);

// Logging
app.use((req: Request, res: Response, next: NextFunction) => {
  console.log(`${new Date().toISOString()} - ${req.method} ${req.path}`);
  next();
});

// ============================================
// SERVICES
// ============================================

const SERVICES = {
  auth: process.env.AUTH_SERVICE_URL || 'http://localhost:8001',
  catalog: process.env.CATALOG_SERVICE_URL || 'http://localhost:8002',
  cart: process.env.CART_SERVICE_URL || 'http://localhost:8003',
  order: process.env.ORDER_SERVICE_URL || 'http://localhost:8004',
  user: process.env.USER_SERVICE_URL || 'http://localhost:8005',
  balance: process.env.BALANCE_SERVICE_URL || 'http://localhost:8006',
  notification: process.env.NOTIFICATION_SERVICE_URL || 'http://localhost:8007',
};

// ============================================
// PROXY CONFIG
// ============================================

const createProxy = (target: string) => {
  return createProxyMiddleware({
    target,
    changeOrigin: true,
    pathRewrite: (path) => path.replace(/^\/api\/[^/]+/, ''),
    onError: (err, req, res) => {
      console.error('Proxy Error:', err);
      (res as Response).status(503).json({
        error: 'Service unavailable'
      });
    }
  });
};

// ============================================
// ROUTES
// ============================================

app.get('/health', (req: Request, res: Response) => {
  res.json({ status: 'healthy', timestamp: new Date().toISOString() });
});

// Auth Service
app.use('/api/auth', createProxy(SERVICES.auth));

// Catalog Service (продукты)
app.use('/api/products', createProxy(SERVICES.catalog));
app.use('/api/categories', createProxy(SERVICES.catalog));

// Cart Service
app.use('/api/cart', createProxy(SERVICES.cart));

// Order Service
app.use('/api/orders', createProxy(SERVICES.order));

// User Service
app.use('/api/users', createProxy(SERVICES.user));

// Balance Service
app.use('/api/balance', createProxy(SERVICES.balance));

// Notification Service
app.use('/api/notifications', createProxy(SERVICES.notification));

// ============================================
// ERROR HANDLING
// ============================================

app.use((req: Request, res: Response) => {
  res.status(404).json({ error: 'Route not found' });
});

app.use((err: Error, req: Request, res: Response, next: NextFunction) => {
  console.error('Error:', err);
  res.status(500).json({ error: 'Internal server error' });
});

// ============================================
// START
// ============================================

app.listen(PORT, () => {
  console.log(`
╔═══════════════════════════════════════════╗
║            API Gateway Running            ║
╠═══════════════════════════════════════════╣
║  Port: ${PORT}
║
║  Services:
║  • Auth:         ${SERVICES.auth}
║  • Catalog:      ${SERVICES.catalog}
║  • Cart:         ${SERVICES.cart}
║  • Order:        ${SERVICES.order}
║  • User:         ${SERVICES.user}
║  • Balance:      ${SERVICES.balance}
║  • Notification: ${SERVICES.notification}
╚═══════════════════════════════════════════╝
  `);
});