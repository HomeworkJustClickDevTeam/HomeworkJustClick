import {useEffect, useState} from "react";
import {useAppDispatch} from "../../types/HooksRedux";
import {setIsLoading} from "../../redux/isLoadingSlice";
import {
    getUserNotifications
} from "../../services/postgresDatabaseServices";
import {Notification} from "../../types/Notification.model";

export  const useGetUserNotifications = (userId: number | undefined | null, pathName: string) => {
    const [notifications,setNotifications] = useState<Notification[]>([])
    const dispatch = useAppDispatch()
    useEffect(() => {
        let mounted = true
        if (userId !== undefined && userId !== null) {
            dispatch(setIsLoading(true))
            getUserNotifications(userId)
                .then((response)=> {
                    if(response?.status === 200)
                        if(mounted){
                           setNotifications(response.data)
                        }
                })
                .catch(error => {
                    if(error?.response.status === 403)
                       setNotifications([])
                })
            dispatch(setIsLoading(false))
        }

        return () => {
            mounted = false
        }
    }, [pathName, userId]);
    return {notifications,setNotifications} as const
}