import React, {useCallback, useState} from "react";


export default function EditableWrapperPaginatedList({  children, initialData = [], onDataChange }) {
    const [editableData, setEditableData] = useState(initialData);

    const addItem = useCallback((newItem) => {
        setEditableData(prevData => [...prevData, newItem]);
        if (onDataChange) onDataChange([...editableData, newItem]);
    }, [editableData, onDataChange]);

    const removeItem = useCallback((itemId) => {
        setEditableData(prevData => prevData.filter(item => item.id !== itemId));
        if (onDataChange) onDataChange(editableData.filter(item => item.id !== itemId));
    }, [editableData, onDataChange]);

    const updateItem = useCallback((updatedItem) => {
        setEditableData(prevData => prevData.map(item => item.id === updatedItem.id ? updatedItem : item));
        if (onDataChange) onDataChange(editableData.map(item => item.id === updatedItem.id ? updatedItem : item));
    }, [editableData, onDataChange]);

    const getPaginatedData = useCallback((page, rowsPerPage) => {
        const startIndex = page * rowsPerPage;
        const endIndex = startIndex + rowsPerPage;
        return editableData.slice(startIndex, endIndex);
    }, [editableData]);

    return React.cloneElement(children, {
        data: editableData,
        totalCount: editableData.length,
        addItem,
        removeItem,
        updateItem,
        getPaginatedData
    });
}