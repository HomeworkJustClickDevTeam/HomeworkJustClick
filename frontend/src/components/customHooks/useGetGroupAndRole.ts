import {useEffect} from "react"
import {useAppDispatch} from "../../types/HooksRedux"
import {setIsLoading} from "../../redux/isLoadingSlice"
import {getGroupPostgresService, getUserRoleInGroupPostgresService} from "../../services/postgresDatabaseServices"
import {setGroup} from "../../redux/groupSlice"
import {setRole} from "../../redux/roleSlice"

export const useGetGroupAndRole = (groupId: number | undefined | null, userId: number | undefined | null) => {
    const dispatch = useAppDispatch()

    useEffect(() => {
        let mounted = true
        dispatch(setIsLoading(true))
        if (groupId !== undefined && groupId !== null && userId !== undefined && userId !== null) {
            getGroupPostgresService(groupId.toString())
                .then((response) => {
                    if (response !== null && response !== undefined) {
                        if (mounted) {
                            dispatch(setGroup(response.data))
                        }
                        getUserRoleInGroupPostgresService(userId.toString(), response.data.id)
                            .then((response) => {
                                if (response !== null && response !== undefined) {
                                    if (mounted) {
                                        dispatch(setRole(response.data))
                                    }
                                }
                            })
                            .catch((error) => {
                                console.log(error)
                                if (mounted) {
                                    dispatch(setRole("User not in group"))
                                }
                            })
                    }
                })
                .catch((error) => {
                    if (error !== null && error !== undefined && error.response.status === 404) {
                        if (mounted) {
                            dispatch(setGroup(null))
                        }
                    } else {
                        console.log("Error fetching group:", error)
                    }
                })
        }
        dispatch(setIsLoading(false))

        return () => {
            mounted = false
        }
    }, [groupId, userId])
}