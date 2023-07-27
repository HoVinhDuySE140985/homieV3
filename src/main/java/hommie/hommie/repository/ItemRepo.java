package hommie.hommie.repository;

import hommie.hommie.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findAllBySubCategory_Id(Long subCateId);
    @Query("select i\n" +
            "from Item i\n" +
            "where i.name Like %:keyWord%")
    List<Item> findAllByKeyWord(String keyWord);

    @Query("select i \n" +
            "From Item i\n" +
            "order by i.createDate desc")
    List<Item> findAll();

    @Query("select i.id \n" +
            "from Category as c join SubCategory as sc on c.id = sc.category.id\n" +
            "join Item as i on i.subCategory.id = sc.id\n" +
            "where c.id = :cateId ")
    List<Integer> findAllByCateId(Long cateId);

}
