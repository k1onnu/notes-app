import React from 'react';

export const Modal = ({ isOpen, onClose, title, children }) => {
    if (!isOpen) return null;

    return (
        <div style={{
            position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
            backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex',
            justifyContent: 'center', alignItems: 'center', zIndex: 1050
        }}>
            <div className="card p-4 shadow" style={{ width: '600px', maxWidth: '90%' }}>
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h5 className="m-0">{title}</h5>
                    <button type="button" className="btn-close" onClick={onClose}></button>
                </div>
                <div>
                    {children}
                </div>
            </div>
        </div>
    );
};