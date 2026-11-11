import React from 'react';

export const Input = ({ label, value, onChange, error, placeholder, type = 'text' }) => {
    return (
        <div className="mb-3">
            {label && <label className="form-label">{label}</label>}
            <input
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                className={`form-control ${error ? 'is-invalid' : ''}`}
            />
            {error && <div className="invalid-feedback">{error}</div>}
        </div>
    );
};