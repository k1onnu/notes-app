import React from 'react';

export const Table = ({ columns, data }) => {
    return (
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
            <thead>
                <tr style={{ borderBottom: '2px solid #ccc', textAlign: 'left' }}>
                    {columns.map((col) => <th key={col.key} style={{ padding: '10px' }}>{col.title}</th>)}
                </tr>
            </thead>
            <tbody>
                {data.map((row, index) => (
                    <tr key={index} style={{ borderBottom: '1px solid #eee' }}>
                        {columns.map((col) => (
                            <td key={col.key} style={{ padding: '10px' }}>
                                {col.render ? col.render(row) : row[col.key]}
                            </td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </table>
    );
};