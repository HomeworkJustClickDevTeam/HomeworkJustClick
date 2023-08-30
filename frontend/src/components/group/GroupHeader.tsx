import {Link, useLocation} from "react-router-dom"
import {useContext} from "react"
import GroupContext from "../../contexts/GroupRoleContext"
import {colorsArray} from "../../assets/colors";
import {GroupListElementPropsInterface} from "../../types/GroupListElementPropsInterface";

//import {theme.colors.colorsArray: string[]} = require("../../../tailwind.config")

function GroupHeader({group}: GroupListElementPropsInterface) {
  const {role} = useContext(GroupContext)
  const location = useLocation();
  const locationSplit = location.pathname.split("/");

  return (
    <div>
      <div className='relative flex text-white justify-center select-none'>
        <div className={`select-none w-[85%] mt-8 h-36 rounded-xl ${colorsArray[group.color]}`}>
          {/* <div className='w-[85%] mt-8 h-36 rounded-xl bg-[#59007f] bg-[#006400] bg-[#800000] bg-[#9A6324] bg-[#469990] bg-[#000075] bg-[#f58231] bg-[#546A7B] bg-[#000000] bg-[#BB9F06] bg-[#3F2A2B] bg-[#565656] bg-[#99621E] bg-[#739E82] bg-[#550C18] bg-[#136F63] bg-[#EF7A85] bg-[#D664BE] bg-[#642CA9] bg-[#03312E]'> */}
          <article className='absolute bottom-0 mb-6 ml-4'>
            <p className='text-3xl'>{group.name}</p>
            <p className='text-xl mt-1'>{group.description}</p>
          </article>
        </div>
      </div>
      <header className='flex inline-block mt-4 mx-[7.5%] justify-between'>
        <div className="text-[22px] font-lato font-medium text-center text-font_gray">
          <ul className="flex flex-wrap -mb-px">
            <li
              className={locationSplit[3] === "assignments" || locationSplit[3] === "solutions" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
              <Link to="assignments" className="inline-block w-[170px] p-4  rounded-t-lg "> Zadania</Link>
            </li>
            <li
              className={locationSplit[3] === "users" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
              <Link to="users" className="inline-block w-[170px] p-4  rounded-t-lg "> Osoby </Link>
            </li>
            <li
              className={locationSplit[3] === "#" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
              <Link to="#" className="inline-block w-[170px] p-4  rounded-t-lg "> Oceny</Link>
            </li>
          </ul>
        </div>
        <div className='relative float-right flex h-16 items-center gap-5 '>
          {role === "Student" ? (
            <Link to="assignments/todo" className=''>Do wykonania</Link>
          ) : (
            <Link to="solutions/uncheck">Do sprawdzenia</Link>
          )}
          {role === "Student" ? (
            <Link to="assignments/done">Zrobione</Link>
          ) : (
            <Link to="solutions/check">Sprawdzone</Link>
          )}
          {role === "Student" ? (
            <Link to="assignments/expired">Zaległe</Link>
          ) : (
            <Link to="solutions/late">Spóźnione</Link>
          )}
          {role === "Teacher" && <Link to="settings">Ustawienia</Link>}
        </div>
      </header>
    </div>

  )
}

export default GroupHeader
