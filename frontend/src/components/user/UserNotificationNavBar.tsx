import {useGetUserNumberNotifications} from "../customHooks/useGetUserNumberNotifications";
import {useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";
import {useLocation} from "react-router-dom";
import {useState} from "react";
import {useGetUserNotifications} from "../customHooks/useGetUserNotificatins";
import {NotificationElementList} from "./NotificationElementList";

export function UserNotificationNavBar() {
    const location = useLocation()
    const userState = useAppSelector(selectUserState)
    const {numberOfNotification,setNumberOfNotification} = useGetUserNumberNotifications(userState?.id, location.pathname);
    const [isMenuOpen, setMenuOpen] = useState(false);
    const {notifications, setNotifications} = useGetUserNotifications(userState?.id, location.pathname)

    const handleMenuToggle = () => {
        setMenuOpen(!isMenuOpen);
    };

    return (<>
        <div onClick={handleMenuToggle}>
            Liczba notyfikacji {numberOfNotification}
        </div>
        {isMenuOpen &&
            <ul>
                {notifications.map((notification) => (
                    <li key={notification.id}>
                        <NotificationElementList description={notification.description}
                                                 groupId={notification.assignment.groupId}
                                                 notificationId={notification.id} setNotifications={setNotifications}
                                                 notifications={notifications}
                                                 setNumberOfNotification={setNumberOfNotification}
                                                 numberOfNotification={numberOfNotification ? numberOfNotification : 0}
                                                 assigmentId={notification.assignment.id}
                                                 setMenuOpen = {setMenuOpen}
                                                 menuOpen = {isMenuOpen}

                        />
                    </li>
                ))}

            </ul>
        }
    </>)
}