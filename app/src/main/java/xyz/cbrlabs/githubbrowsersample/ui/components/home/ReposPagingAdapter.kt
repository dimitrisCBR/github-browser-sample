package xyz.cbrlabs.githubbrowsersample.ui.components.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cbrlabs.githubbrowsersample.databinding.ItemRepoBinding
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo

/**
 * A simple implementation of [PagingDataAdapter] to use with Paging 3 library
 * Currently unused
 */
class ReposPagingAdapter(
    private val onItemClicked: (Repo) -> Unit
) : PagingDataAdapter<Repo, RepoViewHolder>(POST_COMPARATOR) {

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder.create(parent).apply {
            this.binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let(onItemClicked)
            }
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem.name == newItem.name
        }
    }
}

class RepoViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: Repo) {
        binding.repoName.text = repo.name
        binding.repoDescription.text = repo.description
        binding.stargazersCount.text = repo.stargazersCount.toString()
    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            return RepoViewHolder(ItemRepoBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }
}