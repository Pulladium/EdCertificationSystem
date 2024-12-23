import logo from './logo.svg';
import './App.css';
import ButtonAppBar from "./components/Appbar";
import PaginatedList from "./components/PaginatedList";

function App() {
  return (
    <div className="App">
      My first React App
        <ButtonAppBar />
        <PaginatedList />
    </div>
  );
}

export default App;
