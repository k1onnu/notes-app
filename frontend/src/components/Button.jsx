import React from 'react';

export const Button = ({ children, onClick, variant = 'primary', type = 'button', style }) => {
    const baseStyle = { padding: '8px 16px', cursor: 'pointer', border: 'none', borderRadius: '4px', margin: '0 4px' };
    const variants = {
        primary: { backgroundColor: '#007bff', color: 'white' },
        danger: { backgroundColor: '#dc3545', color: 'white' }
    };

    return (
        <button type={type} onClick={onClick} style={{ ...baseStyle, ...variants[variant], ...style }}>
            {children}
        </button>
    );
};