import { Link, useLocation, useNavigate } from "react-router-dom"
import React, { useEffect, useState } from "react"
import { colorsArray } from "../../assets/colors"
import Loading from "../animations/Loading"
import { selectGroup } from "../../redux/groupSlice"
import { selectRole } from "../../redux/roleSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { FaCaretDown, FaCog } from "react-icons/fa";
import { GroupHeaderSortButtonValues } from "../../types/GroupHeaderStortButtonValues";

//import {theme.colors.colorsArray: string[]} = require("../../../tailwind.config")

const studentOptions = [
  { link: "assignments/todo", label: "Do wykonania" },
  { link: "assignments", label: "Wszystkie" },
  { link: "assignments/done", label: "Zrobione" },
  { link: "assignments/expired", label: "Zaległe" }
];
const teacherOptions = [
  { link: "solutions/uncheck", label: "Do sprawdzenia" },
  { link: "assignments", label: "Wszystkie" },
  { link: "solutions/check", label: "Sprawdzone" },
  { link: "solutions/late", label: "Spóźnione" },
  { link: "evaluation/reported", label: "Zgłoszone oceny" },

];

function GroupHeader() {
  const location = useLocation()
  const navigate = useNavigate()
  const [locationSplit, setLocationSplit] = useState<string[]>(location.pathname.split("/"))
  const group = useAppSelector(selectGroup)
  const role = useAppSelector(selectRole)
  const [sortButtonState, setSortButtonState] = useState<GroupHeaderSortButtonValues>(role === "Teacher" ? GroupHeaderSortButtonValues.uncheck : GroupHeaderSortButtonValues.toDo)
  const options = role === "student" ? studentOptions : teacherOptions
  const [selectedOption, setSelectedOption] = useState(options[0])
  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const value = event.target.value;
    const option = options.find((opt) => opt.label === value)
    setSelectedOption(option!)
    navigate(option!.link)
    event.target.blur()
  };

  useEffect(() => {
    setLocationSplit(location.pathname.split("/"))
    switch (location.pathname.split("/")[4]) {
      case "todo":
        setSortButtonState(GroupHeaderSortButtonValues.toDo)
        break
      case "done":
        setSortButtonState(GroupHeaderSortButtonValues.done)
        break
      case "expired":
        setSortButtonState(GroupHeaderSortButtonValues.expired)
        break
      case "uncheck":
        setSortButtonState(GroupHeaderSortButtonValues.uncheck)
        break
      case "check":
        setSortButtonState(GroupHeaderSortButtonValues.check)
        break
      case "late":
        setSortButtonState(GroupHeaderSortButtonValues.late)
        break
      case "reported":
        setSortButtonState(GroupHeaderSortButtonValues.reported)
        break
      default:
        if (location.pathname.split("/")[3] === "assignments") setSortButtonState(GroupHeaderSortButtonValues.assignments)
        else if (location.pathname.split("/")[3] === undefined) {
          if (role === "Teacher") navigate("solutions/uncheck")
          else if (role === "Student") navigate("assignments/todo")
        }
    }
  }, [location, role])

  if (group !== null) {
    return (
      <div>
        <div className='relative flex text-white justify-center select-none'>
          <div className={`relative select-none w-[85%] mt-8 h-36 rounded-xl ${colorsArray[group?.color]}`}>
            {/* <div className='w-[85%] mt-8 h-36 rounded-xl bg-[#59007f] bg-[#006400] bg-[#800000] bg-[#9A6324] bg-[#469990] bg-[#000075] bg-[#f58231] bg-[#546A7B] bg-[#000000] bg-[#BB9F06] bg-[#3F2A2B] bg-[#565656] bg-[#99621E] bg-[#739E82] bg-[#550C18] bg-[#136F63] bg-[#EF7A85] bg-[#D664BE] bg-[#642CA9] bg-[#03312E]'> */}
            <article className='absolute bottom-0 mb-8 ml-4'>
              <p className='text-3xl'>{group.name}</p>
              <p className='text-xl mt-1'>{group.description}</p>
            </article>
            {role === "Teacher" && <Link to="/settings " className="absolute right-6 top-4">
              <FaCog className="m-2" />
            </Link>}
            {role === "Teacher" && <Link to={"assignments"} state={{ groupReport: true }} className='absolute bottom-2 right-8 underline underline-offset-4'>Stwórz raport grupy</Link>}
          </div>
        </div>
        <header className='flex inline-block mt-4 mx-[7.5%] justify-between'>
          <div className="text-[22px] font-lato font-medium text-center text-font_gray">
            <ul className="flex flex-wrap -mb-px">
              <li
                className={(locationSplit[3] === "assignments" && locationSplit[4] === undefined) || locationSplit[3] === "solutions" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                <Link to="assignments" className="inline-block w-[170px] p-4  rounded-t-lg "> Zadania</Link>
              </li>
              <li
                className={locationSplit[3] === "users" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                <Link to="users" className="inline-block w-[170px] p-4  rounded-t-lg "> Osoby </Link>
              </li>
              {role === "Student" ?
                <li
                  className={locationSplit[3] === "evaluations" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                  <Link to="evaluations" className="inline-block w-[170px] p-4  rounded-t-lg "> Oceny</Link>
                </li>
                : <li
                  className={locationSplit[4] === "add" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                  <Link to="assignments/add" className="inline-block w-[205px] p-4  rounded-t-lg "> Nowe Zadanie +</Link>
                </li>}
            </ul>
          </div>
          <div className='relative float-right flex h-16 items-center gap-5 '>
            <select
              className="flex border-2 border-main_blue text-main_blue pl-2 rounded-md xl:rounded-lg w-44 xl:w-56 h-8 xl:h-10 items-center"
              value={selectedOption.label}
              onChange={handleChange}
            >
              {options.map((option) => (
                <option key={option.label}>
                  {option.label}
                </option>
              ))}
            </select>

            {/*{role === "Student" ? (*/}
            {/*  <Link to="assignments/todo" className=''>Do wykonania</Link>*/}
            {/*) : (*/}
            {/*  <Link to="solutions/uncheck">Do sprawdzenia</Link>*/}
            {/*)}*/}
            {/*{role === "Student" ? (*/}
            {/*  <Link to="assignments/done">Zrobione</Link>*/}
            {/*) : (*/}
            {/*  <Link to="solutions/check">Sprawdzone</Link>*/}
            {/*)}*/}
            {/*{role === "Student" ? (*/}
            {/*  <Link to="assignments/expired">Zaległe</Link>*/}
            {/*) : (*/}
            {/*  <Link to="solutions/late">Spóźnione</Link>*/}
            {/*)}*/}

          </div>
        </header >
      </div >

    )
  }
  else {
    return <Loading></Loading>
  }
}

export default GroupHeader
