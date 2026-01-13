import { ReactNode } from 'react';
import Header from './Header';
import Footer from './Footer';

interface LayoutProps {
  children: ReactNode;
}

export default function Layout({ children }: LayoutProps) {
  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      flexDirection: 'column'
    }}>
      <Header />
      <main style={{
        flex: 1,
        paddingTop: '2rem',
        paddingBottom: '4rem'
      }}>
        {children}
      </main>
      <Footer />
    </div>
  );
}