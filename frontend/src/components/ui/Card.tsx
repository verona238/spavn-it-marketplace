import { ReactNode, CSSProperties } from 'react';

interface CardProps {
  children: ReactNode;
  hover?: boolean;
  style?: CSSProperties;
  className?: string;
}

export default function Card({ children, hover = false, style, className = '' }: CardProps) {
  return (
    <div
      className={`card ${className}`}
      style={{
        ...style,
        ...(hover && {
          cursor: 'pointer',
          transition: 'var(--transition)',
        }),
      }}
    >
      {children}
    </div>
  );
}