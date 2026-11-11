import React from 'react';

export const Button = ({ children, onClick, variant = 'primary', type = 'button', className = '' }) => {
    const btnClass = variant === 'danger' ? 'btn-danger' : 'btn-primary';

    return (
        <button type={type} onClick={onClick} className={`btn ${btnClass} ${className}`}>
            {children}
        </button>
    );
};