export default function EditableWrapper({ initialData, children }) {
    const [editableData, setEditableData] = useState(initialData);

    return children({ data: editableData, totalCount: editableData.length, setData: setEditableData });
}