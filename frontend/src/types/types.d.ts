import {Dispatch, SetStateAction} from "react";

interface RegisterUser {
    firstname: string,
    lastname: string,
    email: string,
    password: string
}

interface LoginUser {
    email: string,
    password: string
}

interface Group {
    id: number,
    name: string,
    description: string,
    color: number
}

interface GroupCreate {
    name: string,
    description: string,
}

interface UserToShow {
    firstname: string,
    lastname: string,
    email: string,
    index: number,
    id: number,
}

interface InGroup {
    student: boolean,
    teacher: boolean
}

type PropsForLogin = {
    loggedIn: boolean,
    setLoggedIn: Dispatch<SetStateAction<boolean>>
}

type PropsForGroupItem = {
    group: Group,
    key: number
}
type PropsForFiltering = {
    setGroups: (groups: Group []) => void
}

interface GroupProp {
    id:string | undefined

}
interface Assigment {
    title:string,
    visible: boolean,
    taskDescription:string,
    completionDatetime: Date,
    id:number
}
interface AssigmentToSend {
    title:string,
    visible: boolean,
    taskDescription:string,
    completionDatetime: Date,
}
interface AssigmentItemProps {
    assigment: Assigment;
    idGroup: string
}
interface UserInGroupProp{
    id:string,
    role:InGroup
}
interface UserItemToDisplay{
    userToShow: UserToShow
}