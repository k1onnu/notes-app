import React from 'react';

export const Input = ({ label, value, onChange, error, placeholder, type = 'text', rightElement }) => {
    return (
        <div className="mb-3">
            {label && <label className="form-label">{label}</label>}

            {/* Используем input-group из Bootstrap для добавления элементов справа */}
            <div className="input-group">
                <input
                    type={type}
                    value={value}
                    onChange={onChange}
                    placeholder={placeholder}
                    className={`form-control ${error ? 'is-invalid' : ''}`}
                />
                {rightElement && (
                    <span className="input-group-text" style={{ cursor: 'pointer' }} onClick={rightElement.onClick}>
                        {rightElement.content}
                    </span>
                )}
            </div>

            {error && <div className="invalid-feedback d-block">{error}</div>}
        </div>
    );
};