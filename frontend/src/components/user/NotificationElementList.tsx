import {useNavigate} from "react-router-dom";
import {deleteUserNotification, readNotification} from "../../services/postgresDatabaseServices";
import {Notification} from "../../types/Notification.model";
import {useAppSelector} from "../../types/HooksRedux";
import {selectRole} from "../../redux/roleSlice";

export function NotificationElementList(props: {
    description: string
    groupId: number
    notificationId: number
    notifications: Notification[]
    setNotifications: (evaluationTable: Notification[]) => void
    setNumberOfNotification: (numberOfNotification: number) => void
    numberOfNotification: number
    assigmentId:number
    setMenuOpen: (menuOpen:boolean) => void
    menuOpen: boolean
}) {
    const navigate = useNavigate()
    const role = useAppSelector(selectRole)
    function goToAssigment() {
        readNotification(props.notificationId).then(
            () => {
                props.setMenuOpen(!props.menuOpen)
                if(role === "Teacher") {
                    navigate('evaluation/reported')
                }else {
                    navigate(`group/${props.groupId}/assignment/${props.assigmentId}`)
                }
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