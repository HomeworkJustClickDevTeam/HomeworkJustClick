import {useEffect, useState} from "react";
import {useAppDispatch} from "../../types/HooksRedux";
import {setIsLoading} from "../../redux/isLoadingSlice";
import {getUserNumberOfNotifications} from "../../services/postgresDatabaseServices";

export const useGetUserNumberNotifications = (userId: number | undefined | null, pathName: string) => {
    const [numberOfNotification, setNumberOfNotification] = useState<number>()
    const dispatch = useAppDispatch()

    useEffect(() => {
        let mounted = true
        if (userId !== undefined && userId !== null) {
            dispatch(setIsLoading(true))
            getUserNumberOfNotifications(userId).then(
                (response) => {
                    if (response.status === 200) {
                        if (mounted) {
                            setNumberOfNotification(response.data)
                        }
                    }
                }
            ).catch(error => {
                if (error.response.status == 403)
                    setNumberOfNotification(0)
            })
            dispatch(setIsLoading(false))
        }
        return () => {
            mounted = false
        }
    }, [userId, pathName])

    return {numberOfNotification,setNumberOfNotification} as const;
}