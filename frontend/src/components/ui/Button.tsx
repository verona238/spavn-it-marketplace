import { ReactNode, ButtonHTMLAttributes } from 'react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode;
  variant?: 'primary' | 'secondary' | 'ghost';
  size?: 'sm' | 'md' | 'lg';
  loading?: boolean;
}

export default function Button({
  children,
  variant = 'primary',
  size = 'md',
  loading = false,
  disabled,
  className = '',
  ...props
}: ButtonProps) {
  const baseStyles = {
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '0.5rem',
    borderRadius: 'var(--radius-md)',
    fontWeight: '500',
    transition: 'var(--transition)',
    border: 'none',
    cursor: disabled || loading ? 'not-allowed' : 'pointer',
    opacity: disabled || loading ? 0.6 : 1,
    whiteSpace: 'nowrap' as const,
  };

  const variants = {
    primary: {
      backgroundColor: 'var(--primary)',
      color: 'white',
      ':hover': {
        backgroundColor: 'var(--primary-dark)',
      },
    },
    secondary: {
      backgroundColor: 'var(--surface)',
      color: 'var(--text-primary)',
      border: '1px solid var(--border)',
    },
    ghost: {
      backgroundColor: 'transparent',
      color: 'var(--text-primary)',
    },
  };

  const sizes = {
    sm: { padding: '0.5rem 1rem', fontSize: '0.875rem' },
    md: { padding: '0.75rem 1.5rem', fontSize: '0.875rem' },
    lg: { padding: '1rem 2rem', fontSize: '1rem' },
  };

  return (
    <button
      className={`btn btn-${variant} ${className}`}
      style={{
        ...baseStyles,
        ...variants[variant],
        ...sizes[size],
      }}
      disabled={disabled || loading}
      {...props}
    >
      {loading && <div className="loading" style={{ width: '16px', height: '16px' }} />}
      {children}
    </button>
  );
}