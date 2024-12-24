


export default function EditableWrapperPaginatedList({ initialData, children }) {
    const [editableData, setEditableData] = useState(initialData);

    // only for new Participant
    const addSimpleItem = (item) => {
        setData(prevData => [...prevData, item]);
        setTotalCount(prevCount => prevCount + 1);
        setPage(0);
    };


    return children({ data: editableData, totalCount: editableData.length, setData: setEditableData });
}