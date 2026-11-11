import React from 'react';

export const Input = ({ label, value, onChange, error, placeholder, type = 'text' }) => {
    return (
        <div style={{ marginBottom: '15px' }}>
            {label && <label style={{ display: 'block', marginBottom: '5px' }}>{label}</label>}
            <input
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                style={{ width: '100%', padding: '8px', border: error ? '1px solid red' : '1px solid #ccc' }}
            />
            {error && <span style={{ color: 'red', fontSize: '12px' }}>{error}</span>}
        </div>
    );
};