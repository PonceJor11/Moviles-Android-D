package com.ponce.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponce.murocampus.data.PostRepository
import com.ponce.murocampus.model.Comentario
import com.ponce.murocampus.model.NuevoPost
import com.ponce.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ---- Lista ----
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    // ---- Filtro (null = Todos) ----
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    // ---- Detalle ----
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()
    private var idDetalle: Int? = null

    // ---- Crear (null = aun no se envio) ----
    private val _publicacion = MutableStateFlow<UiState<Post>?>(null)
    val publicacion: StateFlow<UiState<Post>?> = _publicacion.asStateFlow()

    init {
        cargar()
    }

    /** Carga la lista respetando el filtro actual (Todos -> listar, usuario -> porUsuario). */
    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            val userId = _usuarioFiltro.value
            val resultado = if (userId == null) repo.listar() else repo.porUsuario(userId)
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun abrir(id: Int) {
        idDetalle = id
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            // Dos llamadas en paralelo; si cualquiera falla, todo el detalle falla
            val (resPost, resComentarios) = coroutineScope {
                val post = async { repo.detalle(id) }
                val comentarios = async { repo.comentarios(id) }
                post.await() to comentarios.await()
            }
            val post = resPost.getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it))
                return@launch
            }
            val comentarios = resComentarios.getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it))
                return@launch
            }
            _detalle.value = UiState.Exito(DetalleUi(post, comentarios))
        }
    }

    /** Por si el proceso se recreo y la pantalla de detalle quedo sin datos. */
    fun asegurarDetalle(id: Int) {
        if (idDetalle != id) abrir(id)
    }

    fun reintentarDetalle() {
        idDetalle?.let { abrir(it) }
    }

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _publicacion.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _publicacion.value = UiState.Exito(it) }
                .onFailure { _publicacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarPublicacion() {
        _publicacion.value = null
    }
}
