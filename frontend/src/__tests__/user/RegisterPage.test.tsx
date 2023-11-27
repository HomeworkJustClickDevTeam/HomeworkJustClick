import renderer from "react-test-renderer";
import { Provider } from "react-redux";
import { setupStore } from "../../redux/store";
import { BrowserRouter } from "react-router-dom";
import HomeGuestPage from "../../components/home/HomeGuestPage";
import RegisterPage from "../../components/user/RegisterPage";



it("checks if register page renders correctly", () => {
  const renderedHomeGuestPage = renderer
    .create(
      <Provider store={setupStore()}>
        <BrowserRouter>
          <RegisterPage></RegisterPage>
        </BrowserRouter>
      </Provider>
    )
  expect(renderedHomeGuestPage).toMatchSnapshot()
});