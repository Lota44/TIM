import * as React from 'react';
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import AppBar from "@mui/material/AppBar";
import EditIcon from '@mui/icons-material/Edit';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    TextField,
    Toolbar
} from "@material-ui/core";
import {useEffect, useState} from "react";
import axios from "axios";
import {Edit} from "@mui/icons-material";

const ALBUM_REST_API_URL = 'http://localhost:8080/api/music_album/';
const TRACK_REST_API_URL = 'http://localhost:8080/api/tracks/';
const TRACK_OF_ALBUM_API_URL = 'http://localhost:8080/api/tracks/';

function DialogWindow(props){
    const [open, setOpen] = useState(false)
    const{operation, id} = props

    const[author, setAuthor] = useState(props.author ? props.author : '')
    const[name, setName] = useState(props.name ? props.name : '')
    const[genre, setGenre] = useState(props.genre ? props.genre : '')

    const[error1, setError1] = useState(false)
    const[error2, setError2] = useState(false)
    const[error3, setError3] = useState(false)

    const handleClickOpen = () => {
        setOpen(true);
        setAuthor(this.props.author ? props.author : '')
        setName(props.name ? props.name : '')
        setGenre(props.genre ? props.genre : '')
    }
    const handleClickClose = (isOk) => {
        if(isOk){
            if(name === ''){
                setError1(true)
                return
            } else {
                setError1(false)
            }
            if(genre === ''){
                setError2(true)
                return
            } else{
                setError2(false)
            }
            if(author === '') {
                setError3(true)
                return
            } else{
                setError3(false)
            }
            operation({genre,author,name,id})
        }
        setOpen(false)
    }
    return(
        <div>
            {props.button === 'album' &&
            <Button color={"secondary"} variant={'contained'} onClick={handleClickOpen} fullWidth>
                Nowy album
            </Button>}
            {props.button === 'utwór' &&
            <Button color={"primary"} variant={'contained'} onClick={handleClickOpen}>
                Dodaj utwór
            </Button>}

            {props.button === 'editAlbum' &&
            <IconButton color={"primary"} variant={'contained'} onClick={handleClickOpen}>
                <EditIcon/>
            </IconButton>}
            {props.button === 'editUtwor' &&
            <IconButton color={"primary"} variant={'contained'} onClick={handleClickOpen}>
                <EditIcon/>
            </IconButton>}

            <Dialog open={open} onClose={handleClickClose}>
                <DialogTitle>{props.button[0] === 'e' ? 'Edytowanie' : 'Dodawanie'} {props.type}u</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Wprowadź dane {props.type}u
                    </DialogContentText>
                    <TextField
                        defaultValue = {props.name}
                        autoFocus
                        margin="dense"
                        id="nazwa"
                        label="Tytuł"
                        type="text"
                        fullWidth
                        required
                        error={error1}
                        variant="standard"
                        onChange={(event) => {
                            setName(event.target.value)
                        }}>
                    </TextField>
                    <TextField
                        defaultValue={props.genre}
                        margin="dense"
                        id="gatunek"
                        label="Gatunek"
                        type="text"
                        fullWidth
                        required
                        error={error2}
                        variant="standard"
                        onChange={(event) => {
                            setGenre(event.target.value)
                        }}>
                    </TextField>
                    <TextField
                        defaultValue={props.author}
                        margin="dense"
                        id="author"
                        label="Autor"
                        type="text"
                        fullWidth
                        required
                        error={error3}
                        variant="standard"
                        onChange={(event) => {
                            setAuthor(event.target.value)
                        }}>
                    </TextField>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => handleClickClose(false)}>Cancel</Button>
                    <Button onClick={() => handleClickClose(true)}>Accept</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

function Row(props){
    const {row} = props;
    const {addAlbum, editAlbum, deleteAlbum} = props
    const {addTrack, editTrack, deleteTrack} = props

    const [open, setOpen] = React.useState(false);

    return(
        <>
            <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
                <TableCell>
                    <IconButton
                        aria-label="expand row"
                        size = "small"
                        onClick={()=>setOpen(!open)}
                    >
                        {open? <KeyboardArrowUpIcon/>: <KeyboardArrowDownIcon/>}
                    </IconButton>
                </TableCell>
                <TableCell component={"th"} scope={"row"}>{row.name}</TableCell>
                <TableCell>{row.author}</TableCell>
                <TableCell>{row.genre}</TableCell>
                <TableCell>{row.releaseDate}</TableCell>
                <TableCell>
                    <DialogWindow button={'editAlbum'}
                                  type={'album'}
                                  operation={{editAlbum}}
                                  id={row.album_id}
                                  name={row.name}
                                  genre={row.genre}
                                  author={row.author}
                    />
                    <IconButton color={"secondary"}
                                variant={'contained'}
                                onClick={()=>deleteAlbum({id: row.album_id})}>
                        <DeleteForeverIcon/>
                    </IconButton>
                </TableCell>
            </TableRow>
            <TableRow>
                <TableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={6}>
                    <Collapse in={open} timeout={"auto"} unmountOnExit>
                        <Box sx={{margin: 1}}>
                            <Box sx={{flexGrow:1, flexDirection: 'column'}}>
                                <Typography variant={"h6"} gutterBottom component={"div"}>
                                    Utwory
                                </Typography>
                                <DialogWindow button={'utowr'}
                                              type={'utwor'}
                                              operation={{addTrack}}
                                />
                            </Box>
                            <Table size={"small"} aria-label={"purchases"}>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Nazwa</TableCell>
                                        <TableCell>Gatunek</TableCell>
                                        <TableCell>Autor</TableCell>
                                        <TableCell>Data wydania</TableCell>
                                        <TableCell>Modyfikuj</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {row?.trackOfAlbum.map((trackRow, index) => (
                                        <TableRow key={trackRow.track_id + index}>
                                            <TableCell component={"th"} scope={"row"}>
                                                {trackRow.name}
                                            </TableCell>
                                            <TableCell>{trackRow.genre}</TableCell>
                                            <TableCell>{trackRow.author}</TableCell>
                                            <TableCell>{trackRow.release_date}</TableCell>
                                            <TableCell>
                                                <DialogWindow button={'editUtwor'}
                                                              type={'utwor'}
                                                              operation={editTrack}
                                                              id={trackRow.track_id}
                                                              name={trackRow.name}
                                                              genre={trackRow.genre}
                                                              author={trackRow.author}
                                                />
                                                <IconButton color={"secondary"}
                                                            variant={'contained'}
                                                            onClick={()=> {
                                                            console.log(trackRow)
                                                            deleteTrack({id: trackRow.track_id})
                                                            }}>
                                                    <DeleteForeverIcon/>
                                                </IconButton>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </Box>
                    </Collapse>
                </TableCell>
            </TableRow>
        </>
    );
}

export default function MyMainComponent(){
    const [albums, setAlbums] = useState([])

    useEffect(async () => {
        await getAlbum();
    }, [])

    const getAlbum = async () => {
        await axios.get(ALBUM_REST_API_URL)
            .then(res => {
                res.data = res.data.sort((a, b) => a.album_id < b.album_id)
                console.log(res.data)
                let array = []

                res.data.forEach(async (item) => {
                    await axios.get(TRACK_OF_ALBUM_API_URL + item.album_id)
                        .then(res => {
                            item.trackOfAlbum = res.data.sort((a, b) => a.track_id < b.track_id)
                            array = [...array, item]

                            setAlbums(array.sort((a, b) => a.album_id - b.album_id))
                        })
                        .catch(e => {
                            console.log('problem with get Album - getTrack')
                        })
                })
            })
            .catch(e => {
                console.log('problem with getAlbum')
            })
    }

    const addAlbum = async (props) => {
        const {author, genre, name} = props
        const date = new Date()
        const release_date =
            date.getFullYear() + '-' +
            (date.getMonth() < 10 ? '0' + date.getMonth() : date.getMonth()) + '-' +
            (date.getDay() < 10 ? '0' + date.getDay() : date.getDay())
        console.log(author, genre, name)

        await axios.post(ALBUM_REST_API_URL, {}, {params: {author, genre, name}})
            .then(res => {
                console.log('album added')
                console.log(res.data)
                getAlbum()
            })
            .catch(e => {
                console.log('problem z addAlbum')
            })
    }

    const editAlbum = async (props) => {
        const {id, name, author, genre} = props
        console.log(id, name, author, genre)

        await axios.put(ALBUM_REST_API_URL + 'update', {}, {
            params: {
                id,
                newName: name,
                newAuthor: author,
                newGenre: genre
            }
        })
            .then(res => {
                console.log('z edytowano album')
                getAlbum()
            })
            .catch(e => {
                console.log('problem z editAlbum')
            })
    }

    const deleteAlbum = async (props) => {
        const {id} = props
        await axios.delete('http://localhost:8080/api/', {params: {albumId: id}})
            .then(res => {
                console.log('usunieto album')
                setAlbums(albums.filter(album => album.album_id !== id))
            })
            .catch(e => {
                console.log('problem z deleteAlbum')
            })
    }

    const addTrack = async (props) => {
        const {author, genre, name, id} = props
        const date = new Date()
        const release_date =
            date.getFullYear() + '-' +
            (date.getMonth() < 10 ? '0' + date.getMonth() : date.getMonth()) + '-' +
            (date.getDay() < 10 ? '0' + date.getDay() : date.getDay())

        await axios.post('http://localhost:8080/api/track', {}, {params: {author, genre, name}})
            .then(async res => {
                console.log('track added')
                await axios.post('http://localhost:8080/api/track_of_album', {}, {
                    params: {
                        album: id,
                        track: res.data.track_id
                    }
                })
                    .then(res => {
                        console.log('przypisano do albumu')
                        getAlbum()
                    })
                    .catch(e => {
                        console.log('problem z przypisaniem do albumu')
                    })
            })
            .catch(e => {
                console.log('problem z addTrack')
            })
    }

    const editTrack = async (props) => {
        const {id, name, author, genre} = props
        console.log(id, name, author, genre)

        await axios.put('http://localhost:8080/api/track/update/{track_id}', {}, {
            params: {
                id,
                newName: name,
                newAuthor: author,
                newGenre: genre
            }
        })
            .then(res => {
                console.log('edytowano utwor')
                getAlbum()
            })
            .catch(e => {
                console.log('problem z editTrack')
            })
    }

    const deleteTrack = async (props) => {
        const {id} = props
        await axios.delete(`http://localhost:8080/api/track/{track_id}?track_id=${id}`)
            .then(res => {
                console.log('usunieto utwor')
                getAlbum()
            })
            .catch(e => {
                console.log('problem z deleteTrack')
            })
    }

    return (
        <>
            <Box sx={{flexGrow: 1}}>
                <AppBar position='static'>
                    <Toolbar>
                        <Typography variant="h6" component="div" sx={{marginRight: 1}}>
                            Muzyka Świata
                        </Typography>
                        <Typography variant="body2" component="div" sx={{flexGrow: 1}}>
                            Witaj na najmniejszej stronie o tematyce muzycznej zrobionej na zaliczenie w 4 terminie...
                        </Typography>
                    </Toolbar>
                </AppBar>
            </Box>

            <DialogWindow
                button={'album'}
                isOpen={false}
                type={'album'}
                operation={addAlbum}
            />

            <TableContainer component={Paper}>
                <Table aria-label="collapsible table">
                    <TableHead>
                        <TableRow>
                            <TableCell/>
                            <TableCell>Nazwa albumu</TableCell>
                            <TableCell>Autor</TableCell>
                            <TableCell>Gatunek</TableCell>
                            <TableCell>Data wydania</TableCell>
                            <TableCell>
                                Akcje
                            </TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {albums.map((row, index) => (
                            <Row key={index} row={row}
                                 addTrack={addTrack}
                                 editTrack={editTrack}
                                 deleteTrack={deleteTrack}

                                 addAlbum={addAlbum}
                                 editAlbum={editAlbum}
                                 deleteAlbum={deleteAlbum}
                            />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </>
    );
}
