import {useState} from "react";

export default function FetchWrapperPaginatedList({ apiEndpoint, children }) {
    const [fetchedData, setFetchedData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);

    // Логика загрузки данных с сервера

    return children({ data: fetchedData, totalCount, setData: setFetchedData });
}