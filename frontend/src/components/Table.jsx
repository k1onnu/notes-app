import React from 'react';

export const Table = ({ columns, data }) => {
    return (
        <table className="table table-striped table-hover mt-3">
            <thead className="table-dark">
                <tr>
                    {columns.map((col) => <th key={col.key}>{col.title}</th>)}
                </tr>
            </thead>
            <tbody>
                {data.map((row, index) => (
                    <tr key={index}>
                        {columns.map((col) => (
                            <td key={col.key}>
                                {col.render ? col.render(row) : row[col.key]}
                            </td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </table>
    );
};