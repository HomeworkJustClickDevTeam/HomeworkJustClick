import {useNavigate} from "react-router-dom";
import {
    checkIfUserIsTeacherInGroup,
    deleteUserNotification,
    readNotification
} from "../../services/postgresDatabaseServices";
import {Notification} from "../../types/Notification.model";
import {useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";

export function NotificationElementList(props: {
    description: string
    groupId: number
    notificationId: number
    notifications: Notification[]
    setNotifications: (evaluationTable: Notification[]) => void
    setNumberOfNotification: (numberOfNotification: number) => void
    numberOfNotification: number
    assigmentId: number
    setMenuOpen: (menuOpen: boolean) => void
    menuOpen: boolean
}) {
    const navigate = useNavigate()
    const userState = useAppSelector(selectUserState)

    function goToAssigment() {
        readNotification(props.notificationId).then(
            () => {
                props.setMenuOpen(!props.menuOpen)
                checkIfUserIsTeacherInGroup(userState?.id as unknown as number, props.groupId).then(
                    (response) => {
                        if (response.data) {
                            navigate('evaluation/reported')
                        } else {
                            navigate(`group/${props.groupId}/assignment/${props.assigmentId}`)
                        }
                    }
                )
            }
        )
    }

    function deleteNotificationFromView() {
        let newTable = props.notifications
        newTable = newTable?.filter(
            (notificationFromTable) => notificationFromTable.id !== props.notificationId
        )

        props.setNumberOfNotification(props?.numberOfNotification - 1)
        props.setNotifications(newTable as Notification[])
    }

    function deleteNotification() {
        deleteUserNotification(props.notificationId).then(() =>
            deleteNotificationFromView())
    }

    return (<div>
        <p onClick={goToAssigment}> {props.description} </p>
        <button onClick={deleteNotification}>DELETE</button>
    </div>)
}