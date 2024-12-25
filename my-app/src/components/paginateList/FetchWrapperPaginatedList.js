import React, {useEffect, useState} from "react";
import keycloak from "../../config/keycloak";

export default function FetchWrapperPaginatedList({ fetchApiEndpoint, children }) {
    const [fetchedData, setFetchedData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchData = async (page, rowsPerPage) => {
        setLoading(true);
        try {
            const response = await fetch(`${fetchApiEndpoint}?page=${page}&size=${rowsPerPage}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${keycloak.token}`
                },
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const result = await response.json();
            setFetchedData(result.content);
            setTotalCount(result.totalElements);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (fetchApiEndpoint) {
            fetchData(0, 5); // Начальная загрузка
        }
    }, [fetchApiEndpoint]);

    return React.cloneElement(children, {
        data: fetchedData,
        totalCount,
        loading,
        error,
        fetchData
    });
}