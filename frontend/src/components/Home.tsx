import {useEffect, useState} from "react";


import LogOut from "./user/Logedd/LogOut";
import GroupDisplayerItem from "./group/GroupDisplayerItem";
import { Link } from "react-router-dom";
import {groupFilter} from "./group/GroupFilter";


function Home({setLoggedIn}: PropsForLogin){

    const [groups,setGroups] = useState<Group []>()

    const {teacherUserGroups,studentsUserGroups,allUserGroups} = groupFilter({setGroups})

    useEffect(allUserGroups, [])

    return(
       <>
       <Link to="/create/group">
       <button type="button">
           Stwórz grupe
       </button>
       </Link>
           <div >
           {
               groups?.map(group =>
                   <GroupDisplayerItem group={group} key={group.id} />
                   )

           }
           </div>
        <button onClick={studentsUserGroups}>Grupy uczniowskie użytkownika</button>
        <button onClick={teacherUserGroups}> Grupy nauczycielskie użytkownika</button>
        <button onClick={allUserGroups}>Wszystkie grupy użytkownika</button>
        <LogOut setLoggedIn={setLoggedIn} />
       </>
    )
}
export default Home